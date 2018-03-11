import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

/**
 * The Sudoku game UI built using java swing.
 */
class GameFrame(sudokuGame: SudokuGame) : JFrame() {

    // 9x9 matrix of JTextFields, each containing String "1" to "9", or empty String
    val cells = Array<Array<JTextField?>>(GRID_SIZE) { arrayOfNulls(GRID_SIZE) }

    init {
        val cp = contentPane

        // ## STEP_6 ## Extract the code that creates the sudoku board in a separate function.
        // ## STEP_9 ## Position the two panels in the layout by using BorderLayout.NORTH and BorderLayout.SOUTH params.
        cp.layout = GridLayout(3, 3)

        val listener = InputListener(sudokuGame, this)

        // Construct 3x3 JPanels and add to the content-pane
        val panels = Array<Array<JPanel?>>(3) { arrayOfNulls(3) }
        for (row in 0 until 3) {
            for (col in 0 until 3) {
                val panel = JPanel()
                panel.border = BorderFactory.createLineBorder(Color.GRAY, 2)
                panels[row][col] = panel
                // Construct 9x9 JTextFields and add to the content-pane
                createBoard(panel, sudokuGame, listener, row * 3, row * 3 + 3, col * 3, col * 3 + 3)
                cp.add(panel)
            }
        }

        // Set the size of the content-pane and pack all the components under this container.
        cp.preferredSize = Dimension(CANVAS_WIDTH, CANVAS_HEIGHT)
        pack()

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Handle window closing
        title = "Sudoku"
        isVisible = true
    }

    // ## STEP_7 ## Modify the newly created function "createSudokuPanel()" to put all UI widgets inside a JPanel object
    // and return it. Remove the cp parameter and add this new panel to the content pane in the init{} block.

    // ## STEP_8 ## Create a function named "createButtonPanel()" which creates a JPanel object, adds a button to it with
    // the label "Start new game" and returns the panel.
    // Add this new panel to the content pane in the init{} block.

    // ## STEP_10 ## Add an action listener to the button created in "createButtonPanel()" function. Place inside the
    // block the code to remove the 2 panels from the content pane and add new ones. When calling createSudokuPanel()
    // make sure you create a new SudokuGame instance in order to get a completely new game.
    // You have to call
    // contentPane.repaint()
    // pack()
    // in order to refresh the UI.

    private fun createBoard(panel: JPanel, sudokuGame: SudokuGame, listener: InputListener, rowStart: Int, rowEnd: Int, colStart: Int, colEnd: Int) {
        panel.layout = GridLayout(3, 3)
        for (row in rowStart until rowEnd) {
            for (col in colStart until colEnd) {
                cells[row][col] = JTextField() // Allocate element of array
                panel.add(cells[row][col])            // ContentPane adds JTextField
                if (!sudokuGame.visibleElements[row][col]) {
                    cells[row][col]?.text = ""     // set to empty string
                    cells[row][col]?.isEditable = true
                    cells[row][col]?.background = HIDDEN_NUMBER_CELL_BGCOLOR
                    cells[row][col]?.addActionListener(listener);   // For all editable rows and cols
                } else {
                    cells[row][col]?.text = sudokuGame.sudokuBoard[row][col].toString() + ""
                    cells[row][col]?.isEditable = false
                    cells[row][col]?.background = VISIBLE_NUMBER_CELL_BGCOLOR
                    cells[row][col]?.foreground = VISIBLE_NUMBER_CELL_TEXT
                }
                // Beautify all the cells
                cells[row][col]?.horizontalAlignment = JTextField.CENTER
                cells[row][col]?.font = FONT_NUMBERS
            }
        }
    }

    companion object {
        private val GRID_SIZE = 9       // Size of the board with cells
        private val CELL_SIZE = 60      // Cell width/height in pixels
        private val CANVAS_WIDTH = CELL_SIZE * GRID_SIZE         // Board width in pixels
        private val CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE        // Board height in pixels
        private val FONT_NUMBERS = Font("Monospaced", Font.BOLD, 20)
        val INCORRECT_NUMBER_CELL_BGCOLOR = Color.RED
        val HIDDEN_NUMBER_CELL_BGCOLOR = Color.LIGHT_GRAY
        val VISIBLE_NUMBER_CELL_BGCOLOR = Color.WHITE
        val VISIBLE_NUMBER_CELL_TEXT = Color.BLACK
    }
}

class InputListener(private val sudokuGame: SudokuGame, private val sudokuFrame: GameFrame) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        // All the 9x9 JTextFields invoke this handler. We need to determine
        // which JTextField (which row and column) is the source for this invocation.
        var rowSelected = -1
        var colSelected = -1

        // Get the source object that fired the event
        val source = e.source as JTextField
        // Scan JTextFields for all rows and columns, and match with the source object
        var found = false
        var row = 0
        while (row < sudokuGame.visibleElements.size && !found) {
            var col = 0
            while (col < sudokuGame.visibleElements[row].size && !found) {
                if (sudokuFrame.cells[row][col] === source) {
                    rowSelected = row
                    colSelected = col
                    found = true  // break the inner/outer loops
                }
                col++
            }
            row++
        }

        val intValue = Integer.valueOf(sudokuFrame.cells[rowSelected][colSelected]?.text)
        if (sudokuGame.isUserInputCorrect(intValue, rowSelected, colSelected)) {
            sudokuGame.visibleElements[rowSelected][colSelected] = true
            source.background = GameFrame.VISIBLE_NUMBER_CELL_BGCOLOR
        } else {
            source.background = GameFrame.INCORRECT_NUMBER_CELL_BGCOLOR
            JOptionPane.showMessageDialog(source.parent, "Try Again!");
            sudokuFrame.cells[rowSelected][colSelected]?.text = ""
        }

        if (sudokuGame.isSolved()) {
            JOptionPane.showMessageDialog(source.parent, "Solved!");
        }
    }
}