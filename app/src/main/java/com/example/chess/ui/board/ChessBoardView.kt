package com.example.chess.ui.board

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.chess.model.BoardTheme
import com.example.chess.model.ChessPiece
import com.example.chess.model.Move
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceTheme
import com.example.chess.model.PieceType
import com.example.chess.model.Position
import com.example.chess.ui.pieces.ChessPieceView
import java.util.UUID

/**
 * Styling specs for tournament chess boards.
 */
data class BoardStyleSpec(
    val lightSquareTop: Color,
    val lightSquareBottom: Color,
    val darkSquareTop: Color,
    val darkSquareBottom: Color,
    val frameOuterGradient: List<Color>,
    val frameBorderColor: Color,
    val innerBevelColor: Color,
    val coordinateColor: Color,
    val cornerAccentColor: Color,
    val lastMoveColor: Color,
    val selectionGlowColor: Color,
    val checkPulseColor: Color
)

fun getBoardStyle(theme: BoardTheme = BoardTheme.WOODEN): BoardStyleSpec = when (theme) {
    BoardTheme.WOODEN -> BoardStyleSpec(
        lightSquareTop = Color(0xFFF6DEB9),
        lightSquareBottom = Color(0xFFDCAE78),
        darkSquareTop = Color(0xFF7A3E1D),
        darkSquareBottom = Color(0xFF472009),
        frameOuterGradient = listOf(
            Color(0xFF3F1D0E),
            Color(0xFF291208),
            Color(0xFF160904)
        ),
        frameBorderColor = Color(0xFF150702),
        innerBevelColor = Color(0xFF8C5424),
        coordinateColor = Color(0xFFE2BA84),
        cornerAccentColor = Color(0xFFFFD700).copy(alpha = 0.35f),
        lastMoveColor = Color(0xFFF59E0B).copy(alpha = 0.35f),
        selectionGlowColor = Color(0xFFFFD700),
        checkPulseColor = Color(0xFFEF4444)
    )
    BoardTheme.VECTOR_BW -> BoardStyleSpec(
        lightSquareTop = Color(0xFFFFFFFF),
        lightSquareBottom = Color(0xFFFFFFFF),
        darkSquareTop = Color(0xFF16171B),
        darkSquareBottom = Color(0xFF0F1013),
        frameOuterGradient = listOf(Color(0xFF000000), Color(0xFF000000)),
        frameBorderColor = Color(0xFF000000),
        innerBevelColor = Color(0xFF000000),
        coordinateColor = Color(0xFF16171B),
        cornerAccentColor = Color.Transparent,
        lastMoveColor = Color(0xFFFACC15).copy(alpha = 0.35f),
        selectionGlowColor = Color(0xFFFFD700),
        checkPulseColor = Color(0xFFEF4444)
    )
}

@Composable
fun ChessBoardView(
    board: Array<Array<ChessPiece?>>,
    selectedPosition: Position?,
    legalMoves: List<Move>,
    lastMove: Move?,
    isCheck: Boolean,
    turn: PieceColor,
    isFlipped: Boolean = false,
    boardTheme: BoardTheme = BoardTheme.WOODEN,
    pieceTheme: PieceTheme = PieceTheme.PLASTIC,
    onSquareClicked: (Position) -> Unit,
    pendingPromotionMove: Move?,
    onPromotionSelected: (PieceType) -> Unit,
    modifier: Modifier = Modifier
) {
    val style = remember(boardTheme) { getBoardStyle(boardTheme) }
    val isWooden = boardTheme == BoardTheme.WOODEN

    val frameLeft = if (isWooden) 10.dp else 1.dp
    val frameRight = if (isWooden) 10.dp else 1.dp
    val frameTop = if (isWooden) 10.dp else 1.dp
    val frameBottom = if (isWooden) 10.dp else 1.dp

    // Pulsing animation for Check state
    val infiniteTransition = rememberInfiniteTransition(label = "checkPulse")
    val checkPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 0.90f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "checkPulseAlpha"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = if (isWooden) 16.dp else 6.dp,
                shape = RoundedCornerShape(if (isWooden) 8.dp else 4.dp),
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
            .clip(RoundedCornerShape(if (isWooden) 8.dp else 4.dp))
            .background(Brush.linearGradient(style.frameOuterGradient))
            .border(
                width = if (isWooden) 2.5.dp else 1.5.dp,
                color = style.frameBorderColor,
                shape = RoundedCornerShape(if (isWooden) 8.dp else 4.dp)
            )
    ) {
        val boardGridSize = maxWidth
        val squareSize = (boardGridSize - frameLeft - frameRight) / 8

        // Corner miter lines for wooden frame
        if (isWooden) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cornerColor = Color(0x33000000)
                val miterWidth = 1.2.dp.toPx()
                drawLine(cornerColor, Offset(0f, 0f), Offset(frameLeft.toPx(), frameTop.toPx()), miterWidth)
                drawLine(cornerColor, Offset(size.width, 0f), Offset(size.width - frameRight.toPx(), frameTop.toPx()), miterWidth)
                drawLine(cornerColor, Offset(0f, size.height), Offset(frameLeft.toPx(), size.height - frameBottom.toPx()), miterWidth)
                drawLine(cornerColor, Offset(size.width, size.height), Offset(size.width - frameRight.toPx(), size.height - frameBottom.toPx()), miterWidth)
            }
        }

        // Inner 8x8 Playfield
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = frameLeft,
                    top = frameTop,
                    end = frameRight,
                    bottom = frameBottom
                )
                .border(
                    width = if (isWooden) 1.5.dp else 1.dp,
                    color = style.innerBevelColor,
                    shape = RoundedCornerShape(if (isWooden) 2.dp else 0.dp)
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                for (displayRow in 0..7) {
                    val r = if (isFlipped) 7 - displayRow else displayRow
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        for (displayCol in 0..7) {
                            val c = if (isFlipped) 7 - displayCol else displayCol
                            val pos = Position(r, c)
                            val piece = board[r][c]
                            val isLight = (r + c) % 2 == 0

                            val squareBrush = remember(isLight, boardTheme) {
                                if (isWooden) {
                                    if (isLight) {
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xFFF9E4C5),
                                                Color(0xFFEBCDA5),
                                                Color(0xFFDCB282)
                                            )
                                        )
                                    } else {
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xFF7D411F),
                                                Color(0xFF623114),
                                                Color(0xFF4C230B)
                                            )
                                        )
                                    }
                                } else {
                                    Brush.verticalGradient(
                                        if (isLight) listOf(style.lightSquareTop, style.lightSquareBottom)
                                        else listOf(style.darkSquareTop, style.darkSquareBottom)
                                    )
                                }
                            }

                            // State conditions
                            val isSelected = selectedPosition == pos
                            val isLastMoveSquare = lastMove != null && (lastMove.from == pos || lastMove.to == pos)
                            val isKingInCheck = isCheck && piece != null && piece.type == PieceType.KING && piece.color == turn
                            val destinationMove = legalMoves.find { it.to == pos }
                            val isLegalDestination = destinationMove != null

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .background(squareBrush)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { onSquareClicked(pos) },
                                contentAlignment = Alignment.Center
                            ) {
                                // Handcrafted natural wood grain & tile bevel
                                if (isWooden) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val grainColor = if (isLight) Color(0xFFB58957).copy(alpha = 0.22f) else Color(0xFF2C1304).copy(alpha = 0.28f)
                                        val strokeW = 1.1.dp.toPx()
                                        
                                        // Subtle inlaid tile edge highlight & bevel
                                        val bevelHighlight = if (isLight) Color(0x33FFFFFF) else Color(0x22FFFFFF)
                                        val bevelShadow = Color(0x33000000)
                                        drawLine(bevelHighlight, Offset(0f, 0f), Offset(size.width, 0f), 0.8.dp.toPx())
                                        drawLine(bevelHighlight, Offset(0f, 0f), Offset(0f, size.height), 0.8.dp.toPx())
                                        drawLine(bevelShadow, Offset(0f, size.height), Offset(size.width, size.height), 0.8.dp.toPx())
                                        drawLine(bevelShadow, Offset(size.width, 0f), Offset(size.width, size.height), 0.8.dp.toPx())

                                        // Organic wavy wood grain streaks (distinct per square)
                                        val seed = ((r * 13 + c * 29) % 10) / 10f
                                        val y1 = size.height * (0.22f + seed * 0.15f)
                                        val y2 = size.height * (0.58f + seed * 0.18f)
                                        val y3 = size.height * (0.85f - seed * 0.12f)

                                        drawLine(
                                            color = grainColor,
                                            start = Offset(0f, y1),
                                            end = Offset(size.width, y1 + size.height * 0.04f),
                                            strokeWidth = strokeW
                                        )
                                        drawLine(
                                            color = grainColor,
                                            start = Offset(0f, y2),
                                            end = Offset(size.width, y2 - size.height * 0.03f),
                                            strokeWidth = strokeW
                                        )
                                        drawLine(
                                            color = grainColor,
                                            start = Offset(0f, y3),
                                            end = Offset(size.width, y3 + size.height * 0.02f),
                                            strokeWidth = strokeW
                                        )
                                    }
                                }

                                // 1. Last Move Ambient Tint
                                if (isLastMoveSquare) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(style.lastMoveColor)
                                    )
                                }

                                // 2. Selection Glow & Accent Border
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.radialGradient(
                                                    listOf(
                                                        style.selectionGlowColor.copy(alpha = 0.50f),
                                                        style.selectionGlowColor.copy(alpha = 0.25f),
                                                        Color.Transparent
                                                    )
                                                )
                                            )
                                            .border(
                                                width = 2.dp,
                                                color = style.selectionGlowColor,
                                                shape = RoundedCornerShape(2.dp)
                                            )
                                    )
                                }

                                // 3. King In Check Crimson Danger Pulse
                                if (isKingInCheck) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.radialGradient(
                                                    listOf(
                                                        style.checkPulseColor.copy(alpha = checkPulseAlpha),
                                                        style.checkPulseColor.copy(alpha = checkPulseAlpha * 0.5f),
                                                        Color.Transparent
                                                    )
                                                )
                                            )
                                    )
                                }

                                // 4. Coordinate labels inside the squares (only for Vector B&W)
                                if (!isWooden) {
                                    val showRank = displayCol == 0
                                    val showFile = displayRow == 7
                                    val coordLabelColor = if (isLight) style.darkSquareTop.copy(alpha = 0.85f) else style.lightSquareTop.copy(alpha = 0.75f)

                                    if (showRank) {
                                        Text(
                                            text = (8 - r).toString(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = FontFamily.SansSerif,
                                            color = coordLabelColor,
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(start = 3.dp, top = 2.dp)
                                        )
                                    }

                                    if (showFile) {
                                        Text(
                                            text = ('a' + c).toString(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = FontFamily.SansSerif,
                                            color = coordLabelColor,
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(end = 3.dp, bottom = 2.dp)
                                        )
                                    }
                                }

                                // 5. Render Chess Piece with 3D tactile elevation
                                if (piece != null) {
                                    ChessPieceView(
                                        piece = piece,
                                        theme = pieceTheme,
                                        isSelected = isSelected,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                // 6. Legal Move Indicators
                                if (isLegalDestination) {
                                    if (piece == null && !destinationMove!!.isEnPassant) {
                                        // Quiet Move Indicator: Sleek glowing dot
                                        Canvas(modifier = Modifier.size(squareSize * 0.32f)) {
                                            // Soft ambient outer ring
                                            drawCircle(
                                                color = Color(0xFF10B981).copy(alpha = 0.30f),
                                                radius = size.minDimension / 2
                                            )
                                            // Crisp inner core
                                            drawCircle(
                                                color = Color(0xFF10B981).copy(alpha = 0.85f),
                                                radius = size.minDimension * 0.32f
                                            )
                                        }
                                    } else {
                                        // Capture Move Indicator: Precision target ring
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFFEF4444).copy(alpha = 0.16f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                                                drawCircle(
                                                    color = Color(0xFFEF4444).copy(alpha = 0.75f),
                                                    style = Stroke(width = 3.5.dp.toPx())
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }



        // Pawn Promotion Modal Dialog
        if (pendingPromotionMove != null) {
            PromotionDialog(
                color = turn,
                pieceTheme = pieceTheme,
                onSelected = onPromotionSelected
            )
        }
    }
}

@Composable
fun PromotionDialog(
    color: PieceColor,
    pieceTheme: PieceTheme,
    onSelected: (PieceType) -> Unit
) {
    Dialog(onDismissRequest = { onSelected(PieceType.QUEEN) }) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF181B26)),
            modifier = Modifier
                .padding(12.dp)
                .border(1.5.dp, Color(0xFFFFD700).copy(alpha = 0.6f), RoundedCornerShape(22.dp))
                .shadow(24.dp, RoundedCornerShape(22.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pawn Promotion",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Select piece to transform into",
                    color = Color(0xFF94A3B8),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                )

                val promoPieces = listOf(
                    PieceType.QUEEN,
                    PieceType.ROOK,
                    PieceType.BISHOP,
                    PieceType.KNIGHT
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    promoPieces.forEach { type ->
                        val dummyPiece = ChessPiece(UUID.randomUUID().toString(), type, color)
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF242838))
                                .border(1.5.dp, Color(0xFF3B4259), RoundedCornerShape(14.dp))
                                .clickable { onSelected(type) }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ChessPieceView(piece = dummyPiece, theme = pieceTheme)
                        }
                    }
                }
            }
        }
    }
}
