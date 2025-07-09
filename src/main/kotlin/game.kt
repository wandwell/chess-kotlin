class Game {
    private val board = Board()
    var whiteTurn: Boolean = true

    fun makeMove(start: Position, end: Position): Boolean {
        val movingPiece = board.getPiece(start)

        val currentColor = if (whiteTurn) PieceColor.WHITE else PieceColor.BLACK
        if (movingPiece == null || movingPiece.color != currentColor) {
            return false
        }

        // Filter legal moves to only those that resolve check
        val legalMoves = movingPiece.getLegalMoves(start, board)
            .filter { !wouldBeInCheckAfterMove(movingPiece, it, currentColor) }

        if (end !in legalMoves) return false

        board.movePiece(start, end)
        whiteTurn = !whiteTurn
        return true
    }

    fun isInCheck(kingColor: PieceColor): Boolean {
        val kingPosition = findKingPosition(kingColor)
        val opponentPieces = board.getBoard().filter { (_, piece) ->
            piece != null && piece.color != kingColor
        }

        return opponentPieces.any { (_, piece) ->
            piece!!.getLegalMoves(piece.position, board).contains(kingPosition)
        }
    }

    private fun findKingPosition(color: PieceColor): Position {
        for ((_, piece) in board.getBoard()) {
            if (piece is King && piece.color == color) {
                return piece.position
            }
        }
        throw IllegalStateException("King of color $color not found on the board.")
    }

    fun isCheckmate(color: PieceColor): Boolean {
        if (!isInCheck(color)) return false

        val playerPieces = board.getBoard().filterValues { it?.color == color }

        for ((_, piece) in playerPieces) {
            if (piece != null) {
                val legalMoves = piece.getLegalMoves(piece.position, board)
                for (target in legalMoves) {
                    if (!wouldBeInCheckAfterMove(piece, target, color)) {
                        return false
                    }
                }
            }
        }

        return true
    }

    fun wouldBeInCheckAfterMove(
        piece: Piece,
        target: Position,
        color: PieceColor
    ): Boolean {
        val originalPosition = piece.position
        val captured = board.getBoard()[target.toString()]

        // Simulate the move
        board.getBoard()[originalPosition.toString()] = null
        board.getBoard()[target.toString()] = piece
        piece.position = target

        val inCheck = isInCheck(color)

        // Undo the move
        board.getBoard()[originalPosition.toString()] = piece
        board.getBoard()[target.toString()] = captured
        piece.position = originalPosition

        return inCheck
    }

    fun getBoard(): Board = board
}