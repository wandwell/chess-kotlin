class Knight(color: PieceColor, position: Position) : Piece(color, position) {
    override fun getLegalMoves(from: Position, board: Board): List<Position> {
        val deltas = listOf(
            1 to 2, 2 to 1, -1 to 2, -2 to 1,
            1 to -2, 2 to -1, -1 to -2, -2 to -1
        )

        return deltas.mapNotNull { (dx, dy) -> from.offset(dx, dy) }
            .filter { board.getPiece(it)?.color != color }
    }
}