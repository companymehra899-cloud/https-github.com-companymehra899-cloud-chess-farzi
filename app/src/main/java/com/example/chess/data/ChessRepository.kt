package com.example.chess.data

import com.example.chess.model.BoardTheme
import com.example.chess.model.ChatMessage
import com.example.chess.model.Friend
import com.example.chess.model.FriendRequest
import com.example.chess.model.GameRecord
import com.example.chess.model.LeaderboardEntry
import com.example.chess.model.PieceTheme
import com.example.chess.model.RequestStatus
import com.example.chess.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ChessRepository {

    private val _userProfile = MutableStateFlow(
        UserProfile(
            id = "guest_7241",
            username = "Guest_Player",
            isGuest = true,
            rating = 1200,
            peakRating = 1260,
            gamesPlayed = 18,
            wins = 11,
            losses = 5,
            draws = 2,
            avatarId = "m_1",
            borderColorId = "gold",
            title = "Rising Star",
            country = "Global",
            preferredBoardTheme = BoardTheme.WOODEN,
            preferredPieceTheme = PieceTheme.PLASTIC
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _friends = MutableStateFlow(
        listOf(
            Friend("u_101", "MagnusKnight", 1850, "m_2", isOnline = true, inGame = false, title = "Grandmaster"),
            Friend("u_102", "Elena_Gambit", 1420, "f_1", isOnline = true, inGame = true, title = "Tactician"),
            Friend("u_103", "CyberBot_3k", 1600, "r_1", isOnline = true, inGame = false, title = "AI Specialist"),
            Friend("u_104", "FoxMaster", 1290, "a_2", isOnline = false, inGame = false, title = "Club Player"),
            Friend("u_105", "PhoenixRook", 1510, "b_5", isOnline = true, inGame = false, title = "Speed Demon")
        )
    )
    val friends: StateFlow<List<Friend>> = _friends.asStateFlow()

    private val _friendRequests = MutableStateFlow(
        listOf(
            FriendRequest(
                id = "req_1",
                fromUserId = "u_201",
                fromUsername = "CheckmateQueen",
                fromAvatarId = "f_4",
                fromRating = 1380,
                isIncoming = true,
                status = RequestStatus.PENDING
            ),
            FriendRequest(
                id = "req_2",
                fromUserId = "u_202",
                fromUsername = "IronPawn",
                fromAvatarId = "r_2",
                fromRating = 1150,
                isIncoming = true,
                status = RequestStatus.PENDING
            ),
            FriendRequest(
                id = "req_3",
                fromUserId = "u_301",
                fromUsername = "EagleEye_GM",
                fromAvatarId = "b_1",
                fromRating = 1720,
                isIncoming = false,
                status = RequestStatus.PENDING
            )
        )
    )
    val friendRequests: StateFlow<List<FriendRequest>> = _friendRequests.asStateFlow()

    private val _leaderboard = MutableStateFlow(
        listOf(
            LeaderboardEntry(1, "Magnus_Carlsen", 2853, "m_1", "World Champion", "🇳🇴"),
            LeaderboardEntry(2, "Hikaru_Nakamura", 2835, "m_2", "Grandmaster", "🇺🇸"),
            LeaderboardEntry(3, "Gukesh_D", 2798, "m_3", "Candidate GM", "🇮🇳"),
            LeaderboardEntry(4, "Elena_Tactics", 2780, "f_1", "Grandmaster", "🇪🇸"),
            LeaderboardEntry(5, "Alireza_F", 2775, "m_4", "Grandmaster", "🇫🇷"),
            LeaderboardEntry(6, "Praggnanandhaa", 2768, "m_5", "Grandmaster", "🇮🇳"),
            LeaderboardEntry(7, "Cyber_DeepBlue", 2750, "r_3", "Engine Master", "🤖"),
            LeaderboardEntry(8, "Sophia_Gambit", 2690, "f_3", "International Master", "🇩🇪"),
            LeaderboardEntry(9, "Swift_Falcon", 2640, "b_2", "Master", "🇬🇧"),
            LeaderboardEntry(10, "Tiger_King", 2610, "a_3", "Master", "🇧🇷")
        )
    )
    val leaderboard: StateFlow<List<LeaderboardEntry>> = _leaderboard.asStateFlow()

    private val _gameHistory = MutableStateFlow(
        listOf(
            GameRecord(
                id = "g_1",
                opponentName = "MagnusKnight",
                opponentAvatarId = "m_2",
                opponentRating = 1850,
                result = "Won",
                ratingDelta = +16,
                moveCount = 38,
                playedAt = "Today, 14:20",
                pgnSummary = "1. e4 e5 2. Nf3 Nc6 3. Bc4 Bc5 4. O-O Nf6 5. d3 d6..."
            ),
            GameRecord(
                id = "g_2",
                opponentName = "CyberBot_3k",
                opponentAvatarId = "r_1",
                opponentRating = 1600,
                result = "Lost",
                ratingDelta = -8,
                moveCount = 45,
                playedAt = "Yesterday",
                pgnSummary = "1. d4 d5 2. c4 e6 3. Nc3 Nf6 4. Bg5 Be7..."
            ),
            GameRecord(
                id = "g_3",
                opponentName = "Elena_Gambit",
                opponentAvatarId = "f_1",
                opponentRating = 1420,
                result = "Won",
                ratingDelta = +12,
                moveCount = 28,
                playedAt = "2 days ago",
                pgnSummary = "1. e4 c5 2. Nf3 d6 3. d4 cxd4 4. Nxd4 Nf6..."
            )
        )
    )
    val gameHistory: StateFlow<List<GameRecord>> = _gameHistory.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    fun updateProfile(
        username: String,
        avatarId: String,
        borderColorId: String,
        title: String
    ) {
        _userProfile.value = _userProfile.value.copy(
            username = username,
            avatarId = avatarId,
            borderColorId = borderColorId,
            title = title
        )
    }

    fun updateThemes(boardTheme: BoardTheme, pieceTheme: PieceTheme) {
        _userProfile.value = _userProfile.value.copy(
            preferredBoardTheme = boardTheme,
            preferredPieceTheme = pieceTheme
        )
    }

    fun recordGameOutcome(won: Boolean, isDraw: Boolean, opponentName: String, opponentAvatarId: String, opponentRating: Int, moveCount: Int) {
        val curr = _userProfile.value
        val delta = if (isDraw) 2 else if (won) 15 else -10
        val newRating = Math.max(400, curr.rating + delta)
        val newPeak = Math.max(curr.peakRating, newRating)

        _userProfile.value = curr.copy(
            rating = newRating,
            peakRating = newPeak,
            gamesPlayed = curr.gamesPlayed + 1,
            wins = if (won && !isDraw) curr.wins + 1 else curr.wins,
            losses = if (!won && !isDraw) curr.losses + 1 else curr.losses,
            draws = if (isDraw) curr.draws + 1 else curr.draws
        )

        val dateFormat = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date())

        val record = GameRecord(
            id = UUID.randomUUID().toString(),
            opponentName = opponentName,
            opponentAvatarId = opponentAvatarId,
            opponentRating = opponentRating,
            result = if (isDraw) "Draw" else if (won) "Won" else "Lost",
            ratingDelta = delta,
            moveCount = moveCount,
            playedAt = dateStr,
            pgnSummary = "1. e4 e5 2. Nf3 Nc6..."
        )
        _gameHistory.value = listOf(record) + _gameHistory.value
    }

    fun acceptFriendRequest(requestId: String) {
        val req = _friendRequests.value.find { it.id == requestId } ?: return
        _friendRequests.value = _friendRequests.value.filter { it.id != requestId }

        val newFriend = Friend(
            id = req.fromUserId,
            username = req.fromUsername,
            rating = req.fromRating,
            avatarId = req.fromAvatarId,
            isOnline = true,
            title = "Friend"
        )
        _friends.value = _friends.value + newFriend
    }

    fun declineFriendRequest(requestId: String) {
        _friendRequests.value = _friendRequests.value.filter { it.id != requestId }
    }

    fun sendFriendRequest(username: String): Boolean {
        if (username.isBlank()) return false
        val newReq = FriendRequest(
            id = UUID.randomUUID().toString(),
            fromUserId = "u_" + UUID.randomUUID().toString().take(6),
            fromUsername = username,
            fromAvatarId = "m_2",
            fromRating = 1300,
            isIncoming = false,
            status = RequestStatus.PENDING
        )
        _friendRequests.value = listOf(newReq) + _friendRequests.value
        return true
    }

    fun addChatMessage(message: ChatMessage) {
        _chatMessages.value = _chatMessages.value + message
    }

    fun clearChat() {
        _chatMessages.value = emptyList()
    }
}
