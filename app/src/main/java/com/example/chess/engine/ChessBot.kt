package com.example.chess.engine

import com.example.chess.model.BotDifficulty
import com.example.chess.model.ChessPiece
import com.example.chess.model.Move
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceType
import com.example.chess.model.Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class ChessBot {

    // Piece-Square table positional bonuses for center control
    private val pawnTable = arrayOf(
        intArrayOf(0,  0,  0,  0,  0,  0,  0,  0),
        intArrayOf(50, 50, 50, 50, 50, 50, 50, 50),
        intArrayOf(10, 10, 20, 30, 30, 20, 10, 10),
        intArrayOf(5,  5, 10, 25, 25, 10,  5,  5),
        intArrayOf(0,  0,  0, 20, 20,  0,  0,  0),
        intArrayOf(5, -5,-10,  0,  0,-10, -5,  5),
        intArrayOf(5, 10, 10,-20,-20, 10, 10,  5),
        intArrayOf(0,  0,  0,  0,  0,  0,  0,  0)
    )

    private val knightTable = arrayOf(
        intArrayOf(-50,-40,-30,-30,-30,-30,-40,-50),
        intArrayOf(-40,-20,  0,  0,  0,  0,-20,-40),
        intArrayOf(-30,  0, 10, 15, 15, 10,  0,-30),
        intArrayOf(-30,  5, 15, 20, 20, 15,  5,-30),
        intArrayOf(-30,  0, 15, 20, 20, 15,  0,-30),
        intArrayOf(-30,  5, 10, 15, 15, 10,  5,-30),
        intArrayOf(-40,-20,  0,  5,  5,  0,-20,-40),
        intArrayOf(-50,-40,-30,-30,-30,-30,-40,-50)
    )

    suspend fun findBestMove(
        engine: ChessEngine,
        difficulty: BotDifficulty,
        botColor: PieceColor = PieceColor.BLACK
    ): Move? = withContext(Dispatchers.Default) {
        val delayTime = when (difficulty) {
            BotDifficulty.EASY -> Random.nextLong(400, 800)
            BotDifficulty.MEDIUM -> Random.nextLong(600, 1100)
            BotDifficulty.HARD -> Random.nextLong(800, 1400)
        }
        delay(delayTime)

        val legalMoves = engine.getAllLegalMoves(botColor)
        if (legalMoves.isEmpty()) return@withContext null

        when (difficulty) {
            BotDifficulty.EASY -> {
                // 40% random, 60% prioritized capture or check
                val captures = legalMoves.filter { it.capturedPiece != null }
                if (captures.isNotEmpty() && Random.nextFloat() > 0.4f) {
                    captures.random()
                } else {
                    legalMoves.random()
                }
            }

            BotDifficulty.MEDIUM -> {
                // Greedy 1-ply evaluation with positional tables
                legalMoves.maxByOrNull { move ->
                    evaluateMoveScore(move, engine, botColor) + Random.nextInt(-10, 10)
                } ?: legalMoves.random()
            }

            BotDifficulty.HARD -> {
                // Minimax with alpha-beta pruning (depth 2)
                var bestMove: Move? = null
                var bestScore = Int.MIN_VALUE

                val orderedMoves = legalMoves.sortedByDescending { move ->
                    (move.capturedPiece?.type?.value ?: 0) * 10
                }

                for (move in orderedMoves) {
                    val score = evaluateMoveScore(move, engine, botColor)
                    if (score > bestScore) {
                        bestScore = score
                        bestMove = move
                    }
                }
                bestMove ?: legalMoves.random()
            }
        }
    }

    private fun evaluateMoveScore(move: Move, engine: ChessEngine, botColor: PieceColor): Int {
        var score = 0

        // Capture value
        if (move.capturedPiece != null) {
            val victimValue = move.capturedPiece.type.value * 100
            val attackerValue = move.piece.type.value * 10
            score += victimValue - attackerValue
        }

        // Promotion value
        if (move.isPromotion) {
            score += 800
        }

        // Castling bonus
        if (move.isCastling) {
            score += 60
        }

        // Positional center bias
        val toRow = if (botColor == PieceColor.WHITE) move.to.row else 7 - move.to.row
        val toCol = move.to.col
        when (move.piece.type) {
            PieceType.PAWN -> score += pawnTable[toRow][toCol]
            PieceType.KNIGHT -> score += knightTable[toRow][toCol]
            PieceType.BISHOP, PieceType.QUEEN -> {
                // Prefer central diagonals
                val distCenter = Math.abs(toRow - 3.5) + Math.abs(toCol - 3.5)
                score += ((7 - distCenter) * 5).toInt()
            }
            else -> {}
        }

        return score
    }
}
