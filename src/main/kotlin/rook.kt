class Rook(color: PieceColor, position: Position) : Piece(color, position) {
    override fun getLegalMoves(from: Position, board: Board): List<Position> {
        return slideMoves(from, board, listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1))
    }
}