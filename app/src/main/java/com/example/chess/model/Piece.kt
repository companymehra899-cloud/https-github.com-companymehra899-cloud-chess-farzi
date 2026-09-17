package com.example.chess.model

enum class PieceType(val symbol: String, val value: Int) {
    PAWN("P", 1),
    KNIGHT("N", 3),
    BISHOP("B", 3),
    ROOK("R", 5),
    QUEEN("Q", 9),
    KING("K", 1000)
}

enum class PieceColor {
    WHITE,
    BLACK;

    fun opposite(): PieceColor = if (this == WHITE) BLACK else WHITE
}

data class Position(val row: Int, val col: Int) {
    val algebraic: String
        get() {
            val file = ('a' + col).toString()
            val rank = (8 - row).toString()
            return "$file$rank"
        }

    fun isValid(): Boolean = row in 0..7 && col in 0..7

    companion object {
        fun fromAlgebraic(notation: String): Position? {
            if (notation.length != 2) return null
            val file = notation[0] - 'a'
            val rank = 8 - (notation[1] - '0')
            return if (file in 0..7 && rank in 0..7) Position(rank, file) else null
        }
    }
}

data class ChessPiece(
    val id: String,
    val type: PieceType,
    val color: PieceColor,
    val hasMoved: Boolean = false
)
