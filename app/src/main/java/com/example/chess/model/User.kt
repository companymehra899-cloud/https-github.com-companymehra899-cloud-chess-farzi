package com.example.chess.model

data class UserProfile(
    val id: String = "guest_8921",
    val username: String = "Guest_Player",
    val isGuest: Boolean = true,
    val rating: Int = 1200,
    val peakRating: Int = 1250,
    val gamesPlayed: Int = 14,
    val wins: Int = 8,
    val losses: Int = 4,
    val draws: Int = 2,
    val avatarId: String = "m_1",
    val borderColorId: String = "gold",
    val title: String = "Rising Star",
    val country: String = "Global",
    val preferredBoardTheme: BoardTheme = BoardTheme.VECTOR_BW,
    val preferredPieceTheme: PieceTheme = PieceTheme.PLASTIC
) {
    val winRate: Int
        get() = if (gamesPlayed > 0) ((wins.toFloat() / gamesPlayed) * 100).toInt() else 0
}

data class Friend(
    val id: String,
    val username: String,
    val rating: Int,
    val avatarId: String,
    val isOnline: Boolean,
    val inGame: Boolean = false,
    val title: String = "Club Player"
)

enum class RequestStatus {
    PENDING,
    ACCEPTED,
    DECLINED
}

data class FriendRequest(
    val id: String,
    val fromUserId: String,
    val fromUsername: String,
    val fromAvatarId: String,
    val fromRating: Int,
    val isIncoming: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val status: RequestStatus = RequestStatus.PENDING
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val senderAvatarId: String,
    val isFromUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class LeaderboardEntry(
    val rank: Int,
    val username: String,
    val rating: Int,
    val avatarId: String,
    val title: String,
    val countryFlag: String,
    val isCurrentUser: Boolean = false
)

data class GameRecord(
    val id: String,
    val opponentName: String,
    val opponentAvatarId: String,
    val opponentRating: Int,
    val result: String, // "Won", "Lost", "Draw"
    val ratingDelta: Int,
    val moveCount: Int,
    val playedAt: String,
    val pgnSummary: String
)
