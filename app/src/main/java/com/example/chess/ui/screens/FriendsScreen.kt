package com.example.chess.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.chess.model.Friend
import com.example.chess.model.FriendRequest
import com.example.chess.model.GameMode
import com.example.chess.ui.avatar.ChessAvatarView
import com.example.chess.viewmodel.ChessViewModel

@Composable
fun FriendsScreen(
    viewModel: ChessViewModel,
    modifier: Modifier = Modifier
) {
    val friends by viewModel.repository.friends.collectAsState()
    val friendRequests by viewModel.repository.friendRequests.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    var showAddFriendDialog by remember { mutableStateOf(false) }

    val incomingRequests = friendRequests.filter { it.isIncoming }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF101118))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Social & Friends",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Button(
                    onClick = { showAddFriendDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Friend", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Tabs: Friends vs Requests
        item {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color(0xFF181A24),
                contentColor = Color(0xFFFFD700),
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            text = "Friends (${friends.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Requests",
                                fontSize = 13.sp,
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                            )
                            if (incomingRequests.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEF4444)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${incomingRequests.size}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }

        // Content
        if (selectedTabIndex == 0) {
            if (friends.isEmpty()) {
                item {
                    Text(
                        text = "No friends added yet. Tap 'Add Friend' to connect!",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            } else {
                items(friends) { friend ->
                    FriendCard(
                        friend = friend,
                        onPlayInvite = {
                            viewModel.startNewGame(
                                mode = GameMode.ONLINE,
                                customOpponent = friend.username,
                                customAvatarId = friend.avatarId,
                                customRating = friend.rating
                            )
                        }
                    )
                }
            }
        } else {
            if (friendRequests.isEmpty()) {
                item {
                    Text(
                        text = "No pending friend requests.",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            } else {
                items(friendRequests) { req ->
                    FriendRequestCard(
                        request = req,
                        onAccept = { viewModel.repository.acceptFriendRequest(req.id) },
                        onDecline = { viewModel.repository.declineFriendRequest(req.id) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Add Friend Dialog
    if (showAddFriendDialog) {
        AddFriendDialog(
            onSend = { username ->
                viewModel.repository.sendFriendRequest(username)
                showAddFriendDialog = false
            },
            onDismiss = { showAddFriendDialog = false }
        )
    }
}

@Composable
fun FriendCard(
    friend: Friend,
    onPlayInvite: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF181A24)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF272A3B), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ChessAvatarView(
                    avatarId = friend.avatarId,
                    size = 46.dp,
                    showOnlineIndicator = true,
                    isOnline = friend.isOnline,
                    borderColor = if (friend.isOnline) Color(0xFF00E676) else Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = friend.username,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${friend.rating}",
                            fontSize = 12.sp,
                            color = Color(0xFFFFD700)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                        Text(
                            text = if (friend.inGame) "In Game" else if (friend.isOnline) "Online" else "Offline",
                            fontSize = 11.sp,
                            color = if (friend.isOnline) Color(0xFF4ADE80) else Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${friend.title}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            Button(
                onClick = onPlayInvite,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Play", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FriendRequestCard(
    request: FriendRequest,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF181A24)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF272A3B), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ChessAvatarView(
                    avatarId = request.fromAvatarId,
                    size = 42.dp,
                    borderColor = Color(0xFF38BDF8)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = request.fromUsername,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = if (request.isIncoming) "Incoming request • Elo ${request.fromRating}" else "Sent request (Pending)",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            if (request.isIncoming) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = onAccept,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Accept", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = onDecline,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Decline", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF334155))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Pending", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                }
            }
        }
    }
}

@Composable
fun AddFriendDialog(
    onSend: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var usernameInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E202B)),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Add Chess Friend",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = "Enter your friend's player username or Guest ID",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                )

                OutlinedTextField(
                    value = usernameInput,
                    onValueChange = { usernameInput = it },
                    placeholder = { Text("e.g. ChessGrandmaster or guest_4921", color = Color(0xFF64748B), fontSize = 13.sp) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF14151D),
                        unfocusedContainerColor = Color(0xFF14151D),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color(0xFFFFD700),
                        unfocusedIndicatorColor = Color(0xFF334155)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            if (usernameInput.isNotBlank()) {
                                onSend(usernameInput.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(10.dp),
                        enabled = usernameInput.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Send Request", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                }
            }
        }
    }
}
