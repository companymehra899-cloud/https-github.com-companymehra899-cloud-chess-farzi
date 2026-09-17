package com.example.chess.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.model.AvatarRepository
import com.example.chess.model.LeaderboardEntry
import com.example.chess.ui.avatar.ChessAvatarView
import com.example.chess.viewmodel.ChessViewModel

@Composable
fun LeaderboardScreen(
    viewModel: ChessViewModel,
    modifier: Modifier = Modifier
) {
    val leaderboard by viewModel.repository.leaderboard.collectAsState()
    val userProfile by viewModel.repository.userProfile.collectAsState()
    val userBorder = AvatarRepository.BORDERS.find { it.id == userProfile.borderColorId }?.color ?: Color(0xFFFFD700)

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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Global Leaderboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Current User Rank Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2234)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, Color(0xFF38BDF8), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "#142",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color(0xFF38BDF8)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        ChessAvatarView(
                            avatarId = userProfile.avatarId,
                            size = 46.dp,
                            borderColor = userBorder
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Your Standing",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = userProfile.username,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${userProfile.rating} Elo",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color(0xFFFFD700)
                        )
                        Text(
                            text = userProfile.title,
                            fontSize = 11.sp,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Top Grandmasters & Legends",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF94A3B8),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Leaderboard List Items
        items(leaderboard) { entry ->
            LeaderboardItemCard(entry = entry)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LeaderboardItemCard(entry: LeaderboardEntry) {
    val rankBadgeColor = when (entry.rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFE2E8F0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color(0xFF64748B)
    }

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
                // Rank Number / Badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(rankBadgeColor.copy(alpha = 0.15f))
                        .border(1.dp, rankBadgeColor.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#${entry.rank}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = rankBadgeColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                ChessAvatarView(
                    avatarId = entry.avatarId,
                    size = 40.dp,
                    borderColor = rankBadgeColor
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = entry.username,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = entry.countryFlag, fontSize = 12.sp)
                    }
                    Text(
                        text = entry.title,
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Text(
                text = "${entry.rating}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color(0xFFFFD700)
            )
        }
    }
}
