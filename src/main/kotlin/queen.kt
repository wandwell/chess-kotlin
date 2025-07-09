class Queen(color: PieceColor, position: Position) : Piece(color, position) {
    
    // get possible moves based on Queen rules
    override fun getLegalMoves(from: Position, board: Board): List<Position> {
        return slideMoves(from, board, listOf(
            1 to 0, -1 to 0, 0 to 1, 0 to -1,
            1 to 1, -1 to 1, 1 to -1, -1 to -1
        ))
    }

}