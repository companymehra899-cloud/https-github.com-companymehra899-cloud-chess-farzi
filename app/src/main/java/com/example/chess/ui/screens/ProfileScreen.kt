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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.chess.model.AvatarRepository
import com.example.chess.model.BoardTheme
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceTheme
import com.example.chess.model.PieceType
import com.example.chess.ui.avatar.AvatarSelectionDialog
import com.example.chess.ui.avatar.ChessAvatarView
import com.example.chess.ui.board.getBoardStyle
import com.example.chess.ui.pieces.getPieceDrawableRes
import com.example.chess.viewmodel.ChessViewModel

@Composable
fun ProfileScreen(
    viewModel: ChessViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.repository.userProfile.collectAsState()

    var showAvatarDialog by remember { mutableStateOf(false) }
    var showEditUsernameDialog by remember { mutableStateOf(false) }

    val userBorder = AvatarRepository.BORDERS.find { it.id == userProfile.borderColorId }?.color ?: Color(0xFFFFD700)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF101118))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Player Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Profile Identity Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF181A24)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF272A3B), RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        ChessAvatarView(
                            avatarId = userProfile.avatarId,
                            size = 80.dp,
                            borderColor = userBorder,
                            onClick = { showAvatarDialog = true }
                        )
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD700))
                                .clickable { showAvatarDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Avatar",
                                tint = Color.Black,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = userProfile.username,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        IconButton(
                            onClick = { showEditUsernameDialog = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit name",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(userBorder.copy(alpha = 0.2f))
                                .border(1.dp, userBorder.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = userProfile.title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = userBorder
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ID: ${userProfile.id}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showAvatarDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26293A)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = userBorder, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Avatar & Aura Customizer", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }

        // Stats Matrix
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF181A24)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF272A3B), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Match Statistics",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(title = "Rating", value = "${userProfile.rating}", highlightColor = Color(0xFFFFD700))
                        StatItem(title = "Peak", value = "${userProfile.peakRating}", highlightColor = Color(0xFF38BDF8))
                        StatItem(title = "Games", value = "${userProfile.gamesPlayed}")
                        StatItem(title = "Win Rate", value = "${userProfile.winRate}%", highlightColor = Color(0xFF4ADE80))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = "Wins: ${userProfile.wins}", fontSize = 12.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.SemiBold)
                        Text(text = "Losses: ${userProfile.losses}", fontSize = 12.sp, color = Color(0xFFF87171), fontWeight = FontWeight.SemiBold)
                        Text(text = "Draws: ${userProfile.draws}", fontSize = 12.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }



        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Avatar Selection Dialog
    if (showAvatarDialog) {
        AvatarSelectionDialog(
            currentAvatarId = userProfile.avatarId,
            currentBorderColorId = userProfile.borderColorId,
            currentTitle = userProfile.title,
            onSave = { avatarId, borderId, title ->
                viewModel.repository.updateProfile(
                    username = userProfile.username,
                    avatarId = avatarId,
                    borderColorId = borderId,
                    title = title
                )
            },
            onDismiss = { showAvatarDialog = false }
        )
    }

    // Edit Username Dialog
    if (showEditUsernameDialog) {
        var newName by remember { mutableStateOf(userProfile.username) }
        Dialog(onDismissRequest = { showEditUsernameDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E202B)),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Change Username",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14151D),
                            unfocusedContainerColor = Color(0xFF14151D),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color(0xFFFFD700),
                            unfocusedIndicatorColor = Color(0xFF334155)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (newName.isNotBlank()) {
                                    viewModel.repository.updateProfile(
                                        username = newName.trim(),
                                        avatarId = userProfile.avatarId,
                                        borderColorId = userProfile.borderColorId,
                                        title = userProfile.title
                                    )
                                    showEditUsernameDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { showEditUsernameDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(title: String, value: String, highlightColor: Color = Color.White) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 11.sp, color = Color(0xFF94A3B8))
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = highlightColor)
    }
}
