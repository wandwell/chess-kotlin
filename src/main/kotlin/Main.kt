import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.scene.layout.VBox

class ChessApp : Application() {
    private val game = Game()
    private var selectedPosition: Position? = null
    private var legalMoves: List<Position> = emptyList()
    private var currentTurn: PieceColor = PieceColor.WHITE
    private lateinit var primaryStageRef: Stage
    private lateinit var statusLabel: Label

   override fun start(primaryStage: Stage) {
        primaryStageRef = primaryStage

        statusLabel = Label()
        statusLabel.font = Font.font(20.0)
        statusLabel.textFill = Color.BLACK
        statusLabel.alignment = Pos.CENTER

        val grid = GridPane()
        grid.alignment = Pos.CENTER

        val layout = VBox(10.0)
        layout.alignment = Pos.CENTER
        layout.children.addAll(statusLabel, grid)

        refreshBoard(grid)

        val scene = Scene(layout)
        primaryStage.title = getTurnTitle()
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun refreshBoard(grid: GridPane) {
        grid.children.clear()

        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val file = 'a' + col
                val rank = row + 1
                val pos = Position(file, rank)
                val piece = game.getBoard().getPiece(pos)

                val square = Rectangle(80.0, 80.0)
                val isSelected = pos == selectedPosition
                val isLegalMove = pos in legalMoves

                square.fill = when {
                    isSelected -> Color.DARKGREEN
                    isLegalMove -> Color.DARKBLUE
                    (row + col) % 2 == 0 -> Color.BLACK
                    else -> Color.GRAY
                }

                val stack = StackPane()
                stack.children.add(square)

                if (piece != null) {
                    val label = Label(getUnicodeSymbol(piece))
                    label.font = Font.font("Segoe UI Symbol", 36.0)
                    label.textFill = if (piece.color == PieceColor.WHITE) Color.WHITESMOKE else Color.BLACK
                    label.effect = DropShadow(8.0, if (piece.color == PieceColor.WHITE)
                        Color.rgb(0, 0, 0, 0.6) else Color.rgb(255, 255, 255))
                    stack.children.add(label)
                }

                stack.setOnMouseClicked {
                    handleClick(pos, grid)
                }

                grid.add(stack, col, 7 - row)
            }
        }

        primaryStageRef.title = getTurnTitle()
    }

    private fun handleClick(pos: Position, grid: GridPane) {
        val board = game.getBoard()
        val selected = selectedPosition

        if (selected == null) {
            val piece = board.getPiece(pos)
            if (piece != null && piece.color == currentTurn) {
                selectedPosition = pos
                legalMoves = piece.getLegalMoves(pos, board)
                    .filter { !game.wouldBeInCheckAfterMove(piece, it, currentTurn) }
            }
        } else {
            if (pos in legalMoves) {
                val moved = game.makeMove(selected, pos)
                if (moved) {
                    currentTurn = if (currentTurn == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
                } else {
                    println("Invalid move — would result in check")
                }
            }
            selectedPosition = null
            legalMoves = emptyList()
        }

        refreshBoard(grid)
        updateStatusMessage()
    }

    private fun updateStatusMessage() {
        val kingInCheck = game.isInCheck(currentTurn)
        val isCheckmate = game.isCheckmate(currentTurn)

        statusLabel.text = when {
            isCheckmate -> "${currentTurn.name.lowercase().replaceFirstChar { it.uppercase() }} is in checkmate! Game Over."
            kingInCheck -> "${currentTurn.name.lowercase().replaceFirstChar { it.uppercase() }} is in check!"
            else -> "${currentTurn.name.lowercase().replaceFirstChar { it.uppercase() }}'s turn"
        }
    }

    private fun getUnicodeSymbol(piece: Piece): String {
        return when (piece) {
            is King   -> "♚"
            is Queen  -> "♛"
            is Rook   -> "♜"
            is Bishop -> "♝"
            is Knight -> "♞"
            is Pawn   -> "♟"
            else      -> ""
        }
    }

    private fun getTurnTitle(): String {
        val name = currentTurn.name.lowercase().replaceFirstChar { it.uppercase() }
        return "Kotlin Chess — $name's Turn"
    }
}

fun main() {
    Application.launch(ChessApp::class.java)
}