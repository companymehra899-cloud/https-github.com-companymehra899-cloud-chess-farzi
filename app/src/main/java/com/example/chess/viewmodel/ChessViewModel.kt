package com.example.chess.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chess.data.ChessRepository
import com.example.chess.engine.ChessBot
import com.example.chess.engine.ChessEngine
import com.example.chess.model.BoardTheme
import com.example.chess.model.BotDifficulty
import com.example.chess.model.ChatMessage
import com.example.chess.model.ChessPiece
import com.example.chess.model.GameMode
import com.example.chess.model.GameResult
import com.example.chess.model.Move
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceTheme
import com.example.chess.model.PieceType
import com.example.chess.model.Position
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

enum class ActiveScreen {
    HOME,
    GAME,
    LEADERBOARD,
    FRIENDS,
    PROFILE
}

class ChessViewModel(
    val repository: ChessRepository = ChessRepository()
) : ViewModel() {

    private val engine = ChessEngine()
    private val bot = ChessBot()

    // Navigation state
    private val _currentScreen = MutableStateFlow(ActiveScreen.HOME)
    val currentScreen: StateFlow<ActiveScreen> = _currentScreen.asStateFlow()

    // Game state
    private val _boardState = MutableStateFlow(engine.board)
    val boardState: StateFlow<Array<Array<ChessPiece?>>> = _boardState.asStateFlow()

    private val _currentTurn = MutableStateFlow(engine.currentTurn)
    val currentTurn: StateFlow<PieceColor> = _currentTurn.asStateFlow()

    private val _selectedPosition = MutableStateFlow<Position?>(null)
    val selectedPosition: StateFlow<Position?> = _selectedPosition.asStateFlow()

    private val _legalMoves = MutableStateFlow<List<Move>>(emptyList())
    val legalMoves: StateFlow<List<Move>> = _legalMoves.asStateFlow()

    private val _lastMove = MutableStateFlow<Move?>(null)
    val lastMove: StateFlow<Move?> = _lastMove.asStateFlow()

    private val _isCheck = MutableStateFlow(false)
    val isCheck: StateFlow<Boolean> = _isCheck.asStateFlow()

    private val _gameResult = MutableStateFlow(GameResult.ONGOING)
    val gameResult: StateFlow<GameResult> = _gameResult.asStateFlow()

    private val _capturedByWhite = MutableStateFlow<List<ChessPiece>>(emptyList())
    val capturedByWhite: StateFlow<List<ChessPiece>> = _capturedByWhite.asStateFlow()

    private val _capturedByBlack = MutableStateFlow<List<ChessPiece>>(emptyList())
    val capturedByBlack: StateFlow<List<ChessPiece>> = _capturedByBlack.asStateFlow()

    private val _moveHistory = MutableStateFlow<List<Move>>(emptyList())
    val moveHistory: StateFlow<List<Move>> = _moveHistory.asStateFlow()

    private val _isBoardFlipped = MutableStateFlow(false)
    val isBoardFlipped: StateFlow<Boolean> = _isBoardFlipped.asStateFlow()

    private val _pendingPromotionMove = MutableStateFlow<Move?>(null)
    val pendingPromotionMove: StateFlow<Move?> = _pendingPromotionMove.asStateFlow()

    // Match info
    private val _activeGameMode = MutableStateFlow(GameMode.VS_BOT)
    val activeGameMode: StateFlow<GameMode> = _activeGameMode.asStateFlow()

    private val _botDifficulty = MutableStateFlow(BotDifficulty.MEDIUM)
    val botDifficulty: StateFlow<BotDifficulty> = _botDifficulty.asStateFlow()

    private val _opponentName = MutableStateFlow("KnightRider AI")
    val opponentName: StateFlow<String> = _opponentName.asStateFlow()

    private val _opponentAvatarId = MutableStateFlow("r_1")
    val opponentAvatarId: StateFlow<String> = _opponentAvatarId.asStateFlow()

    private val _opponentRating = MutableStateFlow(1350)
    val opponentRating: StateFlow<Int> = _opponentRating.asStateFlow()

    // Timers (in seconds)
    private val _whiteTimeSec = MutableStateFlow(600) // 10 mins
    val whiteTimeSec: StateFlow<Int> = _whiteTimeSec.asStateFlow()

    private val _blackTimeSec = MutableStateFlow(600)
    val blackTimeSec: StateFlow<Int> = _blackTimeSec.asStateFlow()

    private var timerJob: Job? = null
    private var botJob: Job? = null

    // Matchmaking modal
    private val _isMatchmaking = MutableStateFlow(false)
    val isMatchmaking: StateFlow<Boolean> = _isMatchmaking.asStateFlow()

    // Chat modal
    private val _isChatOpen = MutableStateFlow(false)
    val isChatOpen: StateFlow<Boolean> = _isChatOpen.asStateFlow()

    init {
        startTimerLoop()
    }

    fun navigateTo(screen: ActiveScreen) {
        _currentScreen.value = screen
    }

    fun openChat() {
        _isChatOpen.value = true
    }

    fun closeChat() {
        _isChatOpen.value = false
    }

    fun flipBoard() {
        _isBoardFlipped.value = !_isBoardFlipped.value
    }

    fun startNewGame(
        mode: GameMode,
        difficulty: BotDifficulty = BotDifficulty.MEDIUM,
        customOpponent: String? = null,
        customAvatarId: String? = null,
        customRating: Int? = null
    ) {
        engine.resetGame()
        _boardState.value = engine.board
        _currentTurn.value = engine.currentTurn
        _selectedPosition.value = null
        _legalMoves.value = emptyList()
        _lastMove.value = null
        _isCheck.value = false
        _gameResult.value = GameResult.ONGOING
        _capturedByWhite.value = emptyList()
        _capturedByBlack.value = emptyList()
        _moveHistory.value = emptyList()
        _whiteTimeSec.value = 600
        _blackTimeSec.value = 600
        _pendingPromotionMove.value = null
        repository.clearChat()

        _activeGameMode.value = mode
        _botDifficulty.value = difficulty

        when (mode) {
            GameMode.ONLINE -> {
                _opponentName.value = customOpponent ?: listOf("Alex_Vanguard", "ChessNinja_99", "Elena_Rook", "VortexGM").random()
                _opponentAvatarId.value = customAvatarId ?: listOf("m_2", "f_2", "a_1", "r_3").random()
                _opponentRating.value = customRating ?: Random.nextInt(1180, 1390)
                _isBoardFlipped.value = false
            }
            GameMode.VS_BOT -> {
                _opponentName.value = customOpponent ?: when (difficulty) {
                    BotDifficulty.EASY -> "Rookie Bot"
                    BotDifficulty.MEDIUM -> "KnightRider AI"
                    BotDifficulty.HARD -> "Grandmaster Stockfish"
                }
                _opponentAvatarId.value = when (difficulty) {
                    BotDifficulty.EASY -> "r_5"
                    BotDifficulty.MEDIUM -> "r_1"
                    BotDifficulty.HARD -> "r_3"
                }
                _opponentRating.value = difficulty.rating
                _isBoardFlipped.value = false
            }
            GameMode.PASS_AND_PLAY -> {
                _opponentName.value = "Player 2"
                _opponentAvatarId.value = "m_3"
                _opponentRating.value = 1200
                _isBoardFlipped.value = false
            }
        }

        navigateTo(ActiveScreen.GAME)
    }

    fun startOnlineMatchmaking() {
        viewModelScope.launch {
            _isMatchmaking.value = true
            delay(1500) // Simulated matchmaking search
            _isMatchmaking.value = false
            val opponents = listOf("Viktor_K", "MagnusJunior", "QueenOfHearts", "FalconMaster")
            val avatars = listOf("m_4", "f_1", "b_2", "a_4")
            val opp = opponents.random()
            val av = avatars.random()
            val rat = Random.nextInt(1150, 1350)
            startNewGame(GameMode.ONLINE, customOpponent = opp, customAvatarId = av, customRating = rat)
            // Send initial greeting in chat
            delay(1000)
            repository.addChatMessage(
                ChatMessage(
                    id = UUID.randomUUID().toString(),
                    senderName = opp,
                    senderAvatarId = av,
                    isFromUser = false,
                    text = "Good luck! Have fun! 🍀"
                )
            )
        }
    }

    fun onSquareClicked(pos: Position) {
        if (_gameResult.value != GameResult.ONGOING) return

        // If it's Bot turn or opponent turn in online, reject clicks for that color
        if (_activeGameMode.value == GameMode.VS_BOT && _currentTurn.value == PieceColor.BLACK) return

        val currentSelected = _selectedPosition.value

        if (currentSelected == null) {
            val piece = engine.pieceAt(pos)
            if (piece != null && piece.color == _currentTurn.value) {
                _selectedPosition.value = pos
                _legalMoves.value = engine.getLegalMovesForPosition(pos)
            }
        } else {
            // Check if tapped on a legal destination
            val move = _legalMoves.value.find { it.to == pos }
            if (move != null) {
                if (move.isPromotion) {
                    // Ask user for promotion piece
                    _pendingPromotionMove.value = move
                } else {
                    performMove(move)
                }
            } else {
                // Tapped on another friendly piece or empty
                val piece = engine.pieceAt(pos)
                if (piece != null && piece.color == _currentTurn.value) {
                    _selectedPosition.value = pos
                    _legalMoves.value = engine.getLegalMovesForPosition(pos)
                } else {
                    _selectedPosition.value = null
                    _legalMoves.value = emptyList()
                }
            }
        }
    }

    fun onPromotionSelected(type: PieceType) {
        val pending = _pendingPromotionMove.value ?: return
        val finalMove = pending.copy(promotionType = type)
        _pendingPromotionMove.value = null
        performMove(finalMove)
    }

    private fun performMove(move: Move) {
        val success = engine.executeMove(move)
        if (success) {
            _boardState.value = engine.board
            _currentTurn.value = engine.currentTurn
            _selectedPosition.value = null
            _legalMoves.value = emptyList()
            _lastMove.value = move
            _capturedByWhite.value = engine.capturedByWhite.toList()
            _capturedByBlack.value = engine.capturedByBlack.toList()
            _moveHistory.value = engine.moveHistory.toList()

            val inCheck = engine.isKingInCheck(_currentTurn.value)
            _isCheck.value = inCheck

            // Check game over
            if (engine.isCheckmate(_currentTurn.value)) {
                val won = _currentTurn.value == PieceColor.BLACK // White won
                _gameResult.value = if (won) GameResult.WHITE_WON_CHECKMATE else GameResult.BLACK_WON_CHECKMATE
                repository.recordGameOutcome(
                    won = won,
                    isDraw = false,
                    opponentName = _opponentName.value,
                    opponentAvatarId = _opponentAvatarId.value,
                    opponentRating = _opponentRating.value,
                    moveCount = _moveHistory.value.size
                )
                return
            } else if (engine.isStalemate(_currentTurn.value)) {
                _gameResult.value = GameResult.DRAW_STALEMATE
                repository.recordGameOutcome(
                    won = false,
                    isDraw = true,
                    opponentName = _opponentName.value,
                    opponentAvatarId = _opponentAvatarId.value,
                    opponentRating = _opponentRating.value,
                    moveCount = _moveHistory.value.size
                )
                return
            }

            // Trigger AI Bot or simulated online opponent move if applicable
            if (_activeGameMode.value == GameMode.VS_BOT && _currentTurn.value == PieceColor.BLACK) {
                triggerBotMove()
            } else if (_activeGameMode.value == GameMode.ONLINE && _currentTurn.value == PieceColor.BLACK) {
                triggerOnlineOpponentMove()
            }
        }
    }

    private fun triggerBotMove() {
        botJob?.cancel()
        botJob = viewModelScope.launch {
            val botMove = bot.findBestMove(engine, _botDifficulty.value, PieceColor.BLACK)
            if (botMove != null && _gameResult.value == GameResult.ONGOING) {
                performMove(botMove)
            }
        }
    }

    private fun triggerOnlineOpponentMove() {
        botJob?.cancel()
        botJob = viewModelScope.launch {
            val opponentMove = bot.findBestMove(engine, BotDifficulty.MEDIUM, PieceColor.BLACK)
            if (opponentMove != null && _gameResult.value == GameResult.ONGOING) {
                performMove(opponentMove)
            }
        }
    }

    fun resignGame() {
        if (_gameResult.value != GameResult.ONGOING) return
        _gameResult.value = GameResult.WHITE_RESIGNED
        repository.recordGameOutcome(
            won = false,
            isDraw = false,
            opponentName = _opponentName.value,
            opponentAvatarId = _opponentAvatarId.value,
            opponentRating = _opponentRating.value,
            moveCount = _moveHistory.value.size
        )
    }

    fun offerDraw() {
        if (_gameResult.value != GameResult.ONGOING) return
        _gameResult.value = GameResult.DRAW_AGREED
        repository.recordGameOutcome(
            won = false,
            isDraw = true,
            opponentName = _opponentName.value,
            opponentAvatarId = _opponentAvatarId.value,
            opponentRating = _opponentRating.value,
            moveCount = _moveHistory.value.size
        )
    }

    fun sendChatMessage(text: String) {
        val user = repository.userProfile.value
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderName = user.username,
            senderAvatarId = user.avatarId,
            isFromUser = true,
            text = text
        )
        repository.addChatMessage(userMsg)

        // Opponent automatic response in Online / Bot mode
        if (_activeGameMode.value != GameMode.PASS_AND_PLAY) {
            viewModelScope.launch {
                delay(Random.nextLong(1200, 2500))
                val replies = when {
                    text.contains("Good luck", ignoreCase = true) -> listOf("You too!", "Good luck! 🍀", "Let's have a great match!")
                    text.contains("Nice", ignoreCase = true) -> listOf("Thanks! 😊", "Appreciate it!", "Sharp vision!")
                    text.contains("Checkmate", ignoreCase = true) -> listOf("Not so fast! 😉", "I have a counter plan!", "Haha we'll see! ♟️")
                    text.contains("Rematch", ignoreCase = true) -> listOf("Definitely! 🔥", "Count me in!", "Let's do it!")
                    else -> listOf("Nice move!", "Well played!", "Thinking...", "Good game!", "Interesting strategy 🤔")
                }
                repository.addChatMessage(
                    ChatMessage(
                        id = UUID.randomUUID().toString(),
                        senderName = _opponentName.value,
                        senderAvatarId = _opponentAvatarId.value,
                        isFromUser = false,
                        text = replies.random()
                    )
                )
            }
        }
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_gameResult.value == GameResult.ONGOING && _currentScreen.value == ActiveScreen.GAME) {
                    if (_currentTurn.value == PieceColor.WHITE) {
                        if (_whiteTimeSec.value > 0) {
                            _whiteTimeSec.value -= 1
                        } else {
                            _gameResult.value = GameResult.WHITE_TIMEOUT
                        }
                    } else {
                        if (_blackTimeSec.value > 0) {
                            _blackTimeSec.value -= 1
                        } else {
                            _gameResult.value = GameResult.BLACK_TIMEOUT
                        }
                    }
                }
            }
        }
    }

    fun togglePieceTheme() {
        val current = repository.userProfile.value.preferredPieceTheme
        repository.updateThemes(repository.userProfile.value.preferredBoardTheme, current)
    }

    fun setPieceTheme(pieceTheme: PieceTheme) {
        repository.updateThemes(repository.userProfile.value.preferredBoardTheme, pieceTheme)
    }

    fun cycleBoardTheme() {
        val current = repository.userProfile.value.preferredBoardTheme
        val themes = BoardTheme.values()
        val next = themes[(current.ordinal + 1) % themes.size]
        repository.updateThemes(next, repository.userProfile.value.preferredPieceTheme)
    }

    fun setBoardTheme(boardTheme: BoardTheme) {
        repository.updateThemes(boardTheme, repository.userProfile.value.preferredPieceTheme)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        botJob?.cancel()
    }
}
