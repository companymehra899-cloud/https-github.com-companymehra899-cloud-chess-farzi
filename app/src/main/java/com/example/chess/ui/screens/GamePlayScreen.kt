package com.example.chess.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.chess.model.AvatarRepository
import com.example.chess.model.ChessPiece
import com.example.chess.model.GameResult
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceTheme
import com.example.chess.ui.avatar.ChessAvatarView
import com.example.chess.ui.board.ChessBoardView
import com.example.chess.ui.pieces.ChessPieceView
import com.example.chess.ui.chat.InGameChatSheet
import com.example.chess.viewmodel.ActiveScreen
import com.example.chess.viewmodel.ChessViewModel

@Composable
fun GamePlayScreen(
    viewModel: ChessViewModel,
    modifier: Modifier = Modifier
) {
    val boardState by viewModel.boardState.collectAsState()
    val selectedPosition by viewModel.selectedPosition.collectAsState()
    val legalMoves by viewModel.legalMoves.collectAsState()
    val lastMove by viewModel.lastMove.collectAsState()
    val isCheck by viewModel.isCheck.collectAsState()
    val turn by viewModel.currentTurn.collectAsState()
    val gameResult by viewModel.gameResult.collectAsState()
    val capturedByWhite by viewModel.capturedByWhite.collectAsState()
    val capturedByBlack by viewModel.capturedByBlack.collectAsState()
    val moveHistory by viewModel.moveHistory.collectAsState()
    val isBoardFlipped by viewModel.isBoardFlipped.collectAsState()
    val pendingPromotionMove by viewModel.pendingPromotionMove.collectAsState()

    val opponentName by viewModel.opponentName.collectAsState()
    val opponentAvatarId by viewModel.opponentAvatarId.collectAsState()
    val opponentRating by viewModel.opponentRating.collectAsState()

    val whiteTime by viewModel.whiteTimeSec.collectAsState()
    val blackTime by viewModel.blackTimeSec.collectAsState()

    val userProfile by viewModel.repository.userProfile.collectAsState()
    val chatMessages by viewModel.repository.chatMessages.collectAsState()
    val isChatOpen by viewModel.isChatOpen.collectAsState()

    val userBorderColor = AvatarRepository.BORDERS.find { it.id == userProfile.borderColorId }?.color ?: Color(0xFFFFD700)

    // Calculate material advantage
    val whiteMaterial = capturedByWhite.sumOf { it.type.value }
    val blackMaterial = capturedByBlack.sumOf { it.type.value }
    val whiteAdvantage = whiteMaterial - blackMaterial
    val blackAdvantage = blackMaterial - whiteMaterial

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF101118))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: Back & Mode header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(ActiveScreen.HOME) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1A1D2B))
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF191D2B),
                    border = BorderStroke(1.dp, Color(0xFF2C344A))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD700))
                        )
                        Text(
                            text = viewModel.activeGameMode.value.displayName.uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp,
                            color = Color(0xFFF1F5F9)
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.openChat() },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1A1D2B))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "Chat",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(18.dp)
                        )
                        if (chatMessages.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }
            }

            // Opponent Player Bar (Black)
            PlayerInfoBar(
                name = opponentName,
                avatarId = opponentAvatarId,
                borderColor = Color(0xFF64748B),
                rating = opponentRating,
                title = "Opponent",
                timeSeconds = blackTime,
                isTurn = turn == PieceColor.BLACK && gameResult == GameResult.ONGOING,
                capturedPieces = capturedByBlack,
                materialAdvantage = if (blackAdvantage > 0) "+$blackAdvantage" else null
            )

            Spacer(modifier = Modifier.height(2.dp))

            // The Chess Board
            ChessBoardView(
                board = boardState,
                selectedPosition = selectedPosition,
                legalMoves = legalMoves,
                lastMove = lastMove,
                isCheck = isCheck,
                turn = turn,
                isFlipped = isBoardFlipped,
                boardTheme = userProfile.preferredBoardTheme,
                pieceTheme = userProfile.preferredPieceTheme,
                onSquareClicked = { viewModel.onSquareClicked(it) },
                pendingPromotionMove = pendingPromotionMove,
                onPromotionSelected = { viewModel.onPromotionSelected(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(2.dp))

            // User Player Bar (White)
            PlayerInfoBar(
                name = userProfile.username,
                avatarId = userProfile.avatarId,
                borderColor = userBorderColor,
                rating = userProfile.rating,
                title = userProfile.title,
                timeSeconds = whiteTime,
                isTurn = turn == PieceColor.WHITE && gameResult == GameResult.ONGOING,
                capturedPieces = capturedByWhite,
                materialAdvantage = if (whiteAdvantage > 0) "+$whiteAdvantage" else null
            )

            // Bottom In-Game Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GameActionButton(
                    label = "Chat",
                    icon = Icons.AutoMirrored.Filled.Chat,
                    onClick = { viewModel.openChat() }
                )
                GameActionButton(
                    label = "Flip",
                    icon = Icons.Default.SwapVert,
                    onClick = { viewModel.flipBoard() }
                )
                GameActionButton(
                    label = "Board",
                    icon = Icons.Default.Palette,
                    onClick = { viewModel.cycleBoardTheme() }
                )
                GameActionButton(
                    label = "Resign",
                    icon = Icons.Default.Flag,
                    onClick = { viewModel.resignGame() }
                )
            }
        }

        // In-game Chat Sheet
        if (isChatOpen) {
            InGameChatSheet(
                messages = chatMessages,
                opponentName = opponentName,
                opponentAvatarId = opponentAvatarId,
                onSendMessage = { viewModel.sendChatMessage(it) },
                onDismiss = { viewModel.closeChat() }
            )
        }

        // Game Over Dialog
        if (gameResult != GameResult.ONGOING) {
            GameOverDialog(
                result = gameResult,
                userWon = gameResult == GameResult.WHITE_WON_CHECKMATE || gameResult == GameResult.BLACK_RESIGNED,
                moveCount = moveHistory.size,
                onRematch = { viewModel.startNewGame(viewModel.activeGameMode.value, viewModel.botDifficulty.value) },
                onHome = { viewModel.navigateTo(ActiveScreen.HOME) }
            )
        }
    }
}

@Composable
fun PlayerInfoBar(
    name: String,
    avatarId: String,
    borderColor: Color,
    rating: Int,
    title: String,
    timeSeconds: Int,
    isTurn: Boolean,
    capturedPieces: List<ChessPiece>,
    materialAdvantage: String?
) {
    val minutes = timeSeconds / 60
    val seconds = timeSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val isTimeLow = timeSeconds in 1..30

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isTurn) Color(0xFF1E2333) else Color(0xFF141622)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isTurn) 1.5.dp else 1.dp,
                color = if (isTurn) Color(0xFFFFD700) else Color(0xFF262B3D),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                ChessAvatarView(
                    avatarId = avatarId,
                    size = 42.dp,
                    borderColor = borderColor
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = "($rating)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFD700)
                        )
                        if (isTurn) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF22C55E).copy(alpha = 0.2f))
                                    .border(0.8.dp, Color(0xFF22C55E).copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "TURN",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp,
                                    color = Color(0xFF86EFAC)
                                )
                            }
                        }
                    }

                    // Captured pieces icons & advantage
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (capturedPieces.isNotEmpty()) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy((-6).dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.widthIn(max = 140.dp)
                            ) {
                                itemsIndexed(capturedPieces) { _, piece ->
                                    Box(
                                        modifier = Modifier.size(20.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ChessPieceView(
                                            piece = piece,
                                            theme = com.example.chess.model.PieceTheme.PLASTIC,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "0 captured",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        if (materialAdvantage != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF15803D).copy(alpha = 0.25f))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = materialAdvantage,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4ADE80)
                                )
                            }
                        }
                    }
                }
            }

            // Digital Tournament Clock
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isTimeLow) Color(0xFF450A0A)
                        else if (isTurn) Color(0xFF0F121C)
                        else Color(0xFF1B1E2B)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isTimeLow) Color(0xFFEF4444)
                        else if (isTurn) Color(0xFFFFD700)
                        else Color(0xFF2E344A),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = timeFormatted,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    letterSpacing = 0.5.sp,
                    color = if (isTimeLow) Color(0xFFFCA5A5)
                    else if (isTurn) Color(0xFFFFD700)
                    else Color(0xFFE2E8F0)
                )
            }
        }
    }
}

@Composable
fun GameActionButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF171A26),
        border = BorderStroke(1.dp, Color(0xFF272C3E)),
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (label == "Resign") Color(0xFFF87171) else Color(0xFFE2E8F0),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (label == "Resign") Color(0xFFF87171) else Color(0xFFCBD5E1)
            )
        }
    }
}

@Composable
fun GameOverDialog(
    result: GameResult,
    userWon: Boolean,
    moveCount: Int,
    onRematch: () -> Unit,
    onHome: () -> Unit
) {
    val titleText = when (result) {
        GameResult.WHITE_WON_CHECKMATE -> "Checkmate! Victory! 🏆"
        GameResult.BLACK_WON_CHECKMATE -> "Checkmate! Defeat"
        GameResult.DRAW_STALEMATE -> "Draw by Stalemate 🤝"
        GameResult.DRAW_AGREED -> "Draw by Agreement 🤝"
        GameResult.WHITE_RESIGNED -> "White Resigned"
        GameResult.BLACK_RESIGNED -> "Opponent Resigned! Victory! 🏆"
        GameResult.WHITE_TIMEOUT -> "Time Out - Defeat"
        GameResult.BLACK_TIMEOUT -> "Opponent Time Out - Victory! 🏆"
        else -> "Game Concluded"
    }

    val ratingDelta = if (userWon) "+15" else if (result == GameResult.DRAW_AGREED || result == GameResult.DRAW_STALEMATE) "+2" else "-10"

    Dialog(onDismissRequest = {}) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1D28)),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titleText,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = if (userWon) Color(0xFFFFD700) else Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Total Moves: $moveCount",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF26293A))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Rating Change: $ratingDelta Elo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (userWon) Color(0xFF4ADE80) else Color(0xFFF87171)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onRematch,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Rematch", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onHome,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Menu", color = Color.White)
                    }
                }
            }
        }
    }
}
