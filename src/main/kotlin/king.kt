import kotlin.math.abs

class King(color: PieceColor, position: Position) : Piece(color, position) {
    override fun getLegalMoves(from: Position, board: Board): List<Position> {
        val moves = mutableListOf<Position>()

        // Normal king moves (1 square in any direction)
        val deltas = listOf(
            1 to 0, -1 to 0, 0 to 1, 0 to -1,
            1 to 1, -1 to 1, 1 to -1, -1 to -1
        )

        for ((dx, dy) in deltas) {
            val target = from.offset(dx, dy)
            if (target != null) {
                val piece = board.getPiece(target)
                if (piece == null || piece.color != color) {
                    moves.add(target)
                }
            }
        }

        // Castling logic
        if (!hasMoved && !board.isInCheck(color)) {
            val rank = from.rank

            // Kingside castling
            val kingsideSquares = listOf("f$rank", "g$rank").map(Position::from)
            val kingsideClear = kingsideSquares.all { board.getPiece(it) == null }
            val kingsideSafe = kingsideSquares.all { !board.isSquareAttacked(it, color.opposite()) }
            val kingsideRook = board.getPiece(Position('h', rank))
            if (kingsideClear && kingsideSafe && kingsideRook is Rook && !kingsideRook.hasMoved) {
                moves.add(Position('g', rank))
            }

            // Queenside castling
            val queensideSquares = listOf("d$rank", "c$rank", "b$rank").map(Position::from)
            val queensideClear = queensideSquares.all { board.getPiece(it) == null }
            val queensideSafe = queensideSquares.take(2).all { !board.isSquareAttacked(it, color.opposite()) }
            val queensideRook = board.getPiece(Position('a', rank))
            if (queensideClear && queensideSafe && queensideRook is Rook && !queensideRook.hasMoved) {
                moves.add(Position('c', rank))
            }
        }

        return moves
    }
}