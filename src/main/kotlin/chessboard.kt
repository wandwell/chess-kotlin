class Board {
    private val board: MutableMap<String, Piece?> = mutableMapOf()

    init {
        initializeBoard()
        setupPieces()
    }

    // fills in the map collection for the board
    private fun initializeBoard() {
        for (file in 'a'..'h') {
            for (rank in 1..8) {
                val position = "$file$rank"
                board[position] = null
            }
        }
    }

    // fills in board collection with pieces in original positions
    private fun setupPieces() {
        board["a1"] = Rook(PieceColor.WHITE, Position('a', 1))
        board["h1"] = Rook(PieceColor.WHITE, Position('h', 1))
        board["a8"] = Rook(PieceColor.BLACK, Position('a', 8))
        board["h8"] = Rook(PieceColor.BLACK, Position('h', 8))

        board["b1"] = Knight(PieceColor.WHITE, Position('b', 1))
        board["g1"] = Knight(PieceColor.WHITE, Position('g', 1))
        board["b8"] = Knight(PieceColor.BLACK, Position('b', 8))
        board["g8"] = Knight(PieceColor.BLACK, Position('g', 8))

        board["c1"] = Bishop(PieceColor.WHITE, Position('c', 1))
        board["f1"] = Bishop(PieceColor.WHITE, Position('f', 1))
        board["c8"] = Bishop(PieceColor.BLACK, Position('c', 8))
        board["f8"] = Bishop(PieceColor.BLACK, Position('f', 8))

        board["d1"] = Queen(PieceColor.WHITE, Position('d', 1))
        board["d8"] = Queen(PieceColor.BLACK, Position('d', 8))

        board["e1"] = King(PieceColor.WHITE, Position('e', 1))
        board["e8"] = King(PieceColor.BLACK, Position('e', 8))

        for (file in 'a'..'h') {
            board["${file}2"] = Pawn(PieceColor.WHITE, Position(file, 2))
            board["${file}7"] = Pawn(PieceColor.BLACK, Position(file, 7))
        }
    }

    //fetches the piece from specific position on board
    fun getPiece(position: Position): Piece? {
        return board[position.toString()]
    }

    // checks if King is in Check
    fun isInCheck(color: PieceColor): Boolean {
        val kingPos = getBoard().values.find { it is King && it.color == color }?.position
            ?: return false

        return isSquareAttacked(kingPos, color.opposite())
    }

    // checks if specific square is under attack
    fun isSquareAttacked(pos: Position, byColor: PieceColor): Boolean {
    return getBoard().values
        .filter { it != null && it.color == byColor && it !is King } //  skip kings
        .any { it!!.getLegalMoves(it.position, this).contains(pos) }
    }

    // fetches complete board collection
    fun getBoard(): MutableMap<String, Piece?> {
        return board
    }

    // allows piece to move
    fun movePiece(start: Position, end: Position) {
        val piece = board[start.toString()] ?: return
        val legalMoves = piece.getLegalMoves(start, this)

        if (end in legalMoves) {
            board[end.toString()] = piece
            board[start.toString()] = null
            piece.position = end
            piece.hasMoved = true

            //  Handle castling
            if (piece is King && kotlin.math.abs(start.file - end.file) == 2) {
                val rank = start.rank
                if (end.file == 'g') {
                    // Kingside
                    val rook = getPiece(Position('h', rank))
                    if (rook != null) {
                        board["f$rank"] = rook
                        board["h$rank"] = null
                        rook.position = Position('f', rank)
                        rook.hasMoved = true
                    }
                } else if (end.file == 'c') {
                    // Queenside
                    val rook = getPiece(Position('a', rank))
                    if (rook != null) {
                        board["d$rank"] = rook
                        board["a$rank"] = null
                        rook.position = Position('d', rank)
                        rook.hasMoved = true
                    }
                }
            }
        }
    }
}