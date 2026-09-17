package com.example.chess.model

data class Move(
    val from: Position,
    val to: Position,
    val piece: ChessPiece,
    val capturedPiece: ChessPiece? = null,
    val isEnPassant: Boolean = false,
    val isCastling: Boolean = false,
    val isPromotion: Boolean = false,
    val promotionType: PieceType = PieceType.QUEEN,
    val notation: String = ""
)

enum class GameMode(val displayName: String, val description: String) {
    ONLINE("Play Online", "Match with global players live"),
    VS_BOT("Play vs AI", "Practice against smart bots"),
    PASS_AND_PLAY("Pass & Play", "Play with a friend on one device")
}

enum class BotDifficulty(val displayName: String, val rating: Int) {
    EASY("Easy (Rookie)", 700),
    MEDIUM("Medium (Club)", 1350),
    HARD("Master (Stockfish)", 2100)
}

enum class GameResult {
    ONGOING,
    WHITE_WON_CHECKMATE,
    BLACK_WON_CHECKMATE,
    DRAW_STALEMATE,
    DRAW_INSUFFICIENT_MATERIAL,
    DRAW_AGREED,
    WHITE_RESIGNED,
    BLACK_RESIGNED,
    WHITE_TIMEOUT,
    BLACK_TIMEOUT
}

enum class BoardTheme(val displayName: String, val subtitle: String = "") {
    WOODEN("Handcrafted Walnut & Maple", "Luxury Beveled Wood with Gold Inlay"),
    VECTOR_BW("Vector Black & White", "Modern High-Contrast Minimalist")
}

enum class PieceTheme(val displayName: String, val subtitle: String = "") {
    PLASTIC("Modern Staunton (Uploaded Set)", "Premium Ivory & Obsidian Poly-Resin")
}
