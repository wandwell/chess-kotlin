class Bishop(color: PieceColor, position: Position) : Piece(color, position) {
    
    // fetches possible diagonal moves from original position
    override fun getLegalMoves(from: Position, board: Board): List<Position> {
        return slideMoves(from, board, listOf(1 to 1, -1 to 1, 1 to -1, -1 to -1))
    }
}