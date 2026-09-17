package com.example.chess.engine

import com.example.chess.model.ChessPiece
import com.example.chess.model.Move
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceType
import com.example.chess.model.Position
import java.util.UUID

class ChessEngine {

    var board: Array<Array<ChessPiece?>> = createInitialBoard()
        private set

    var currentTurn: PieceColor = PieceColor.WHITE
        private set

    var moveHistory: MutableList<Move> = mutableListOf()
        private set

    var capturedByWhite: MutableList<ChessPiece> = mutableListOf()
        private set

    var capturedByBlack: MutableList<ChessPiece> = mutableListOf()
        private set

    var enPassantTarget: Position? = null
        private set

    var whiteKingHasMoved: Boolean = false
        private set
    var blackKingHasMoved: Boolean = false
        private set
    var whiteRookA1Moved: Boolean = false
        private set
    var whiteRookH1Moved: Boolean = false
        private set
    var blackRookA8Moved: Boolean = false
        private set
    var blackRookH8Moved: Boolean = false
        private set

    fun resetGame() {
        board = createInitialBoard()
        currentTurn = PieceColor.WHITE
        moveHistory.clear()
        capturedByWhite.clear()
        capturedByBlack.clear()
        enPassantTarget = null
        whiteKingHasMoved = false
        blackKingHasMoved = false
        whiteRookA1Moved = false
        whiteRookH1Moved = false
        blackRookA8Moved = false
        blackRookH8Moved = false
    }

    fun pieceAt(pos: Position): ChessPiece? = if (pos.isValid()) board[pos.row][pos.col] else null

    fun getLegalMovesForPosition(pos: Position): List<Move> {
        val piece = pieceAt(pos) ?: return emptyList()
        if (piece.color != currentTurn) return emptyList()
        val pseudoMoves = generatePseudoLegalMoves(pos, board, enPassantTarget)
        return pseudoMoves.filter { move ->
            isMoveLegal(move, board, currentTurn)
        }
    }

    fun getAllLegalMoves(color: PieceColor = currentTurn): List<Move> {
        val moves = mutableListOf<Move>()
        for (r in 0..7) {
            for (c in 0..7) {
                val piece = board[r][c]
                if (piece != null && piece.color == color) {
                    val posMoves = generatePseudoLegalMoves(Position(r, c), board, enPassantTarget)
                    for (m in posMoves) {
                        if (isMoveLegal(m, board, color)) {
                            moves.add(m)
                        }
                    }
                }
            }
        }
        return moves
    }

    fun isKingInCheck(color: PieceColor = currentTurn, customBoard: Array<Array<ChessPiece?>> = board): Boolean {
        val kingPos = findKing(color, customBoard) ?: return false
        return isSquareAttacked(kingPos, color.opposite(), customBoard)
    }

    fun isCheckmate(color: PieceColor = currentTurn): Boolean {
        return isKingInCheck(color) && getAllLegalMoves(color).isEmpty()
    }

    fun isStalemate(color: PieceColor = currentTurn): Boolean {
        return !isKingInCheck(color) && getAllLegalMoves(color).isEmpty()
    }

    fun executeMove(move: Move): Boolean {
        val legalMoves = getLegalMovesForPosition(move.from)
        val validMove = legalMoves.find { it.to == move.to } ?: return false

        // Check for promotion requirement
        val actualMove = if (validMove.isPromotion && move.promotionType != PieceType.PAWN) {
            validMove.copy(promotionType = move.promotionType)
        } else {
            validMove
        }

        applyMoveToBoard(actualMove, board)

        // Handle captured pieces
        if (actualMove.capturedPiece != null) {
            if (currentTurn == PieceColor.WHITE) {
                capturedByWhite.add(actualMove.capturedPiece)
            } else {
                capturedByBlack.add(actualMove.capturedPiece)
            }
        }

        // Update castling rights
        if (actualMove.piece.type == PieceType.KING) {
            if (currentTurn == PieceColor.WHITE) whiteKingHasMoved = true else blackKingHasMoved = true
        }
        if (actualMove.piece.type == PieceType.ROOK) {
            if (actualMove.from == Position(7, 0)) whiteRookA1Moved = true
            if (actualMove.from == Position(7, 7)) whiteRookH1Moved = true
            if (actualMove.from == Position(0, 0)) blackRookA8Moved = true
            if (actualMove.from == Position(0, 7)) blackRookH8Moved = true
        }

        // Update en-passant target
        if (actualMove.piece.type == PieceType.PAWN && Math.abs(actualMove.to.row - actualMove.from.row) == 2) {
            val epRow = (actualMove.from.row + actualMove.to.row) / 2
            enPassantTarget = Position(epRow, actualMove.from.col)
        } else {
            enPassantTarget = null
        }

        // Generate algebraic notation
        val notation = formatAlgebraicNotation(actualMove)
        val finalMove = actualMove.copy(notation = notation)
        moveHistory.add(finalMove)

        // Switch turn
        currentTurn = currentTurn.opposite()
        return true
    }

    private fun applyMoveToBoard(move: Move, b: Array<Array<ChessPiece?>>) {
        val piece = b[move.from.row][move.from.col] ?: move.piece
        b[move.from.row][move.from.col] = null

        // Handle Castling Rook move
        if (move.isCastling) {
            if (move.to == Position(7, 6)) { // White Kingside
                b[7][5] = b[7][7]
                b[7][7] = null
            } else if (move.to == Position(7, 2)) { // White Queenside
                b[7][3] = b[7][0]
                b[7][0] = null
            } else if (move.to == Position(0, 6)) { // Black Kingside
                b[0][5] = b[0][7]
                b[0][7] = null
            } else if (move.to == Position(0, 2)) { // Black Queenside
                b[0][3] = b[0][0]
                b[0][0] = null
            }
        }

        // Handle En Passant capture removal
        if (move.isEnPassant) {
            val capturedPawnRow = move.from.row
            val capturedPawnCol = move.to.col
            b[capturedPawnRow][capturedPawnCol] = null
        }

        // Handle Promotion
        if (move.isPromotion) {
            b[move.to.row][move.to.col] = ChessPiece(
                id = UUID.randomUUID().toString(),
                type = move.promotionType,
                color = piece.color,
                hasMoved = true
            )
        } else {
            b[move.to.row][move.to.col] = piece.copy(hasMoved = true)
        }
    }

    private fun isMoveLegal(move: Move, originalBoard: Array<Array<ChessPiece?>>, color: PieceColor): Boolean {
        val cloneBoard = cloneBoard(originalBoard)
        applyMoveToBoard(move, cloneBoard)
        return !isKingInCheck(color, cloneBoard)
    }

    private fun generatePseudoLegalMoves(
        pos: Position,
        b: Array<Array<ChessPiece?>>,
        epTarget: Position?
    ): List<Move> {
        val piece = b[pos.row][pos.col] ?: return emptyList()
        val moves = mutableListOf<Move>()
        val color = piece.color
        val r = pos.row
        val c = pos.col

        when (piece.type) {
            PieceType.PAWN -> {
                val dir = if (color == PieceColor.WHITE) -1 else 1
                val startRow = if (color == PieceColor.WHITE) 6 else 1
                val promoRow = if (color == PieceColor.WHITE) 0 else 7

                // 1 square forward
                val fwd1 = Position(r + dir, c)
                if (fwd1.isValid() && b[fwd1.row][fwd1.col] == null) {
                    val isPromo = fwd1.row == promoRow
                    moves.add(Move(pos, fwd1, piece, isPromotion = isPromo))
                    // 2 squares forward from starting rank
                    if (r == startRow) {
                        val fwd2 = Position(r + 2 * dir, c)
                        if (b[fwd2.row][fwd2.col] == null) {
                            moves.add(Move(pos, fwd2, piece))
                        }
                    }
                }

                // Diagonal captures
                val capCols = listOf(c - 1, c + 1)
                for (capCol in capCols) {
                    val capPos = Position(r + dir, capCol)
                    if (capPos.isValid()) {
                        val targetPiece = b[capPos.row][capPos.col]
                        if (targetPiece != null && targetPiece.color != color) {
                            val isPromo = capPos.row == promoRow
                            moves.add(Move(pos, capPos, piece, capturedPiece = targetPiece, isPromotion = isPromo))
                        } else if (epTarget != null && capPos == epTarget) {
                            val epCapturedPiece = b[r][capCol]
                            moves.add(Move(pos, capPos, piece, capturedPiece = epCapturedPiece, isEnPassant = true))
                        }
                    }
                }
            }

            PieceType.KNIGHT -> {
                val offsets = listOf(
                    Pair(-2, -1), Pair(-2, 1), Pair(-1, -2), Pair(-1, 2),
                    Pair(1, -2), Pair(1, 2), Pair(2, -1), Pair(2, 1)
                )
                for ((dr, dc) in offsets) {
                    val target = Position(r + dr, c + dc)
                    if (target.isValid()) {
                        val occupant = b[target.row][target.col]
                        if (occupant == null) {
                            moves.add(Move(pos, target, piece))
                        } else if (occupant.color != color) {
                            moves.add(Move(pos, target, piece, capturedPiece = occupant))
                        }
                    }
                }
            }

            PieceType.BISHOP -> generateRays(pos, piece, b, listOf(Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1)), moves)
            PieceType.ROOK -> generateRays(pos, piece, b, listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)), moves)
            PieceType.QUEEN -> generateRays(pos, piece, b, listOf(
                Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1),
                Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1)
            ), moves)

            PieceType.KING -> {
                val steps = listOf(
                    Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
                    Pair(0, -1), Pair(0, 1),
                    Pair(1, -1), Pair(1, 0), Pair(1, 1)
                )
                for ((dr, dc) in steps) {
                    val target = Position(r + dr, c + dc)
                    if (target.isValid()) {
                        val occupant = b[target.row][target.col]
                        if (occupant == null) {
                            moves.add(Move(pos, target, piece))
                        } else if (occupant.color != color) {
                            moves.add(Move(pos, target, piece, capturedPiece = occupant))
                        }
                    }
                }

                // Castling check
                val kingMoved = if (color == PieceColor.WHITE) whiteKingHasMoved else blackKingHasMoved
                if (!kingMoved && !isKingInCheck(color, b)) {
                    val rank = if (color == PieceColor.WHITE) 7 else 0
                    val rookKingsideMoved = if (color == PieceColor.WHITE) whiteRookH1Moved else blackRookH8Moved
                    val rookQueensideMoved = if (color == PieceColor.WHITE) whiteRookA1Moved else blackRookA8Moved

                    // Kingside Castling
                    if (!rookKingsideMoved && b[rank][5] == null && b[rank][6] == null &&
                        !isSquareAttacked(Position(rank, 5), color.opposite(), b) &&
                        !isSquareAttacked(Position(rank, 6), color.opposite(), b)) {
                        moves.add(Move(pos, Position(rank, 6), piece, isCastling = true))
                    }

                    // Queenside Castling
                    if (!rookQueensideMoved && b[rank][1] == null && b[rank][2] == null && b[rank][3] == null &&
                        !isSquareAttacked(Position(rank, 3), color.opposite(), b) &&
                        !isSquareAttacked(Position(rank, 2), color.opposite(), b)) {
                        moves.add(Move(pos, Position(rank, 2), piece, isCastling = true))
                    }
                }
            }
        }
        return moves
    }

    private fun generateRays(
        pos: Position,
        piece: ChessPiece,
        b: Array<Array<ChessPiece?>>,
        directions: List<Pair<Int, Int>>,
        moves: MutableList<Move>
    ) {
        for ((dr, dc) in directions) {
            var currR = pos.row + dr
            var currC = pos.col + dc
            while (currR in 0..7 && currC in 0..7) {
                val target = Position(currR, currC)
                val occupant = b[currR][currC]
                if (occupant == null) {
                    moves.add(Move(pos, target, piece))
                } else {
                    if (occupant.color != piece.color) {
                        moves.add(Move(pos, target, piece, capturedPiece = occupant))
                    }
                    break
                }
                currR += dr
                currC += dc
            }
        }
    }

    private fun findKing(color: PieceColor, b: Array<Array<ChessPiece?>>): Position? {
        for (r in 0..7) {
            for (c in 0..7) {
                val p = b[r][c]
                if (p != null && p.type == PieceType.KING && p.color == color) {
                    return Position(r, c)
                }
            }
        }
        return null
    }

    private fun isSquareAttacked(square: Position, attackerColor: PieceColor, b: Array<Array<ChessPiece?>>): Boolean {
        // Check Knight attacks
        val knightOffsets = listOf(
            Pair(-2, -1), Pair(-2, 1), Pair(-1, -2), Pair(-1, 2),
            Pair(1, -2), Pair(1, 2), Pair(2, -1), Pair(2, 1)
        )
        for ((dr, dc) in knightOffsets) {
            val r = square.row + dr
            val c = square.col + dc
            if (r in 0..7 && c in 0..7) {
                val p = b[r][c]
                if (p != null && p.color == attackerColor && p.type == PieceType.KNIGHT) return true
            }
        }

        // Check Pawn attacks
        val pawnDir = if (attackerColor == PieceColor.WHITE) 1 else -1 // attacker moves forward towards square
        val pawnR = square.row + pawnDir
        for (pawnC in listOf(square.col - 1, square.col + 1)) {
            if (pawnR in 0..7 && pawnC in 0..7) {
                val p = b[pawnR][pawnC]
                if (p != null && p.color == attackerColor && p.type == PieceType.PAWN) return true
            }
        }

        // Check King adjacent attacks
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val r = square.row + dr
                val c = square.col + dc
                if (r in 0..7 && c in 0..7) {
                    val p = b[r][c]
                    if (p != null && p.color == attackerColor && p.type == PieceType.KING) return true
                }
            }
        }

        // Check Diagonals (Bishop / Queen)
        val diagDirs = listOf(Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1))
        for ((dr, dc) in diagDirs) {
            var currR = square.row + dr
            var currC = square.col + dc
            while (currR in 0..7 && currC in 0..7) {
                val p = b[currR][currC]
                if (p != null) {
                    if (p.color == attackerColor && (p.type == PieceType.BISHOP || p.type == PieceType.QUEEN)) {
                        return true
                    }
                    break
                }
                currR += dr
                currC += dc
            }
        }

        // Check Orthogonals (Rook / Queen)
        val orthoDirs = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
        for ((dr, dc) in orthoDirs) {
            var currR = square.row + dr
            var currC = square.col + dc
            while (currR in 0..7 && currC in 0..7) {
                val p = b[currR][currC]
                if (p != null) {
                    if (p.color == attackerColor && (p.type == PieceType.ROOK || p.type == PieceType.QUEEN)) {
                        return true
                    }
                    break
                }
                currR += dr
                currC += dc
            }
        }

        return false
    }

    private fun formatAlgebraicNotation(move: Move): String {
        if (move.isCastling) {
            return if (move.to.col == 6) "O-O" else "O-O-O"
        }
        val sb = StringBuilder()
        if (move.piece.type != PieceType.PAWN) {
            sb.append(move.piece.type.symbol)
        } else if (move.capturedPiece != null) {
            sb.append(('a' + move.from.col))
        }
        if (move.capturedPiece != null) {
            sb.append("x")
        }
        sb.append(move.to.algebraic)
        if (move.isPromotion) {
            sb.append("=").append(move.promotionType.symbol)
        }
        return sb.toString()
    }

    private fun cloneBoard(src: Array<Array<ChessPiece?>>): Array<Array<ChessPiece?>> {
        return Array(8) { r ->
            Array(8) { c ->
                src[r][c]
            }
        }
    }

    companion object {
        fun createInitialBoard(): Array<Array<ChessPiece?>> {
            val b = Array(8) { Array<ChessPiece?>(8) { null } }

            // Black Major Pieces (Row 0)
            val majorTypes = listOf(
                PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
                PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
            )
            for (c in 0..7) {
                b[0][c] = ChessPiece("b_${majorTypes[c]}_$c", majorTypes[c], PieceColor.BLACK)
                b[1][c] = ChessPiece("b_pawn_$c", PieceType.PAWN, PieceColor.BLACK)
            }

            // White Major Pieces (Row 7)
            for (c in 0..7) {
                b[6][c] = ChessPiece("w_pawn_$c", PieceType.PAWN, PieceColor.WHITE)
                b[7][c] = ChessPiece("w_${majorTypes[c]}_$c", majorTypes[c], PieceColor.WHITE)
            }

            return b
        }
    }
}
