enum class PieceColor {
    BLACK, WHITE;

    fun opposite(): PieceColor =
        if (this == WHITE) BLACK else WHITE
}

data class Position(val file: Char, val rank: Int) {
    companion object {
        //turns a string from board map into Position class
        fun from(notation: String): Position {
            require(notation.length == 2) { "Invalid notation: $notation" }
            val file = notation[0]
            val rank = notation[1].digitToInt()
            return Position(file, rank)
        }
    }

    //allows position to change based on integer input
    fun offset(dx: Int, dy: Int): Position? {
        val newFile = file + dx
        val newRank = rank + dy
        return if (newFile in 'a'..'h' && newRank in 1..8) Position(newFile, newRank) else null
    }

    override fun toString(): String = "$file$rank"
}

abstract class Piece(
    val color: PieceColor,
    var position: Position,
    var hasMoved: Boolean = false
) {
    // to be overridden by non-abstract classes
    abstract fun getLegalMoves(from: Position, board: Board): List<Position>
}

// basic movement for Queen, Bishop, and Rook
fun slideMoves(
        from: Position,
        board: Board,
        directions: List<Pair<Int, Int>>
    ): List<Position> {
        val moves = mutableListOf<Position>()

        for ((dx, dy) in directions) {
            var current = from.offset(dx, dy)
            while (current != null) {
                val target = board.getPiece(current)

                if (target == null) {
                    moves.add(current)
                } else {
                    if (target.color != board.getPiece(from)?.color) {
                        moves.add(current)
                    }
                    break
                }

                current = current.offset(dx, dy)
            }
        }

        return moves
    }