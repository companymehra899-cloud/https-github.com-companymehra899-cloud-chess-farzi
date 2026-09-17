package com.example.chess.ui.pieces

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.chess.model.ChessPiece
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceTheme
import com.example.chess.model.PieceType

@Composable
fun ChessPieceView(
    piece: ChessPiece,
    theme: PieceTheme = PieceTheme.PLASTIC,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val drawableRes = getPieceDrawableRes(piece.type, piece.color, theme)
    val painter = painterResource(id = drawableRes)

    // Selected piece lift animation for tactile 3D pickup effect
    val liftElevation by animateDpAsState(
        targetValue = if (isSelected) (-8).dp else 0.dp,
        label = "liftElevation"
    )
    val selectionScaleBoost by animateFloatAsState(
        targetValue = if (isSelected) 1.06f else 1.0f,
        label = "selectionScale"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Main Modern Staunton Piece (Natural proportions matching original reference image)
        Image(
            painter = painter,
            contentDescription = "${piece.color} ${piece.type}",
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp, top = 0.dp)
                .offset(y = liftElevation)
                .graphicsLayer {
                    scaleX = selectionScaleBoost * 1.18f
                    scaleY = selectionScaleBoost * 1.22f
                    transformOrigin = TransformOrigin(0.5f, 0.98f)
                },
            contentScale = ContentScale.Fit
        )
    }
}

@DrawableRes
fun getPieceDrawableRes(
    type: PieceType,
    color: PieceColor,
    theme: PieceTheme = PieceTheme.PLASTIC
): Int {
    val isWhite = color == PieceColor.WHITE
    return if (isWhite) {
        when (type) {
            PieceType.KING -> R.drawable.piece_plastic_white_king
            PieceType.QUEEN -> R.drawable.piece_plastic_white_queen
            PieceType.ROOK -> R.drawable.piece_plastic_white_rook
            PieceType.BISHOP -> R.drawable.piece_plastic_white_bishop
            PieceType.KNIGHT -> R.drawable.piece_plastic_white_knight
            PieceType.PAWN -> R.drawable.piece_plastic_white_pawn
        }
    } else {
        when (type) {
            PieceType.KING -> R.drawable.piece_plastic_black_king
            PieceType.QUEEN -> R.drawable.piece_plastic_black_queen
            PieceType.ROOK -> R.drawable.piece_plastic_black_rook
            PieceType.BISHOP -> R.drawable.piece_plastic_black_bishop
            PieceType.KNIGHT -> R.drawable.piece_plastic_black_knight
            PieceType.PAWN -> R.drawable.piece_plastic_black_pawn
        }
    }
}
