class Pawn(color: PieceColor, position: Position) : Piece(color, position) {
    
    // gets legal moves according to pawn rules
    override fun getLegalMoves(from: Position, board: Board): List<Position> {
        val moves = mutableListOf<Position>()
        val direction = if (color == PieceColor.WHITE) 1 else -1

        val oneStep = from.offset(0, direction)
        if (oneStep != null && board.getPiece(oneStep) == null) {
            moves.add(oneStep)

            //allows the pawn to move 2 spaces if it hasn't moved before
            val startRank = if (color == PieceColor.WHITE) 2 else 7
            val twoStep = from.offset(0, 2 * direction)
            if (from.rank == startRank && twoStep != null && board.getPiece(twoStep) == null) {
                moves.add(twoStep)
            }
        }

        for (dx in listOf(-1, 1)) {
            val diag = from.offset(dx, direction)
            if (diag != null) {
                val target = board.getPiece(diag)
                if (target != null && target.color != color) {
                    moves.add(diag)
                }
            }
        }

        return moves
    }
}