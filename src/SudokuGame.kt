import java.io.File

/**
 * The Sudoku logic.
 */
class SudokuGame {

    // ## STEP_1 ## Create a res folder inside src and move sudoku_1 file there.

    // ## STEP_2 ## Add 9 more sudoku_x files inside src/res folder.


    // This property holds a 2D array of integers with the initial Sudoku board.
    val sudokuBoard = readSudokuBoard()

    // This property holds a 2D array of booleans. It is used to show or hide a value from our Sudoku board.
    val visibleElements = initVisibleElements()

    // ## STEP_3 ## Create below a private function that returns a random file name along with relative path from the ones
    // we added to the res folder. Name it "randomFileName".
    // The return type should be a String and the value should be "res/sudoku_x", where x is any number between 1 and 10.

    // ## STEP_4 ## Modify the body of the below function to call the "randomFileName()" function instead of the hardcoded
    // path when assigning the sudokuFile variable. Make sure you keep "./src" path and append the file name to it.
    // ## STEP_5 ## We need to make some more changes to the body of this function as we're planning to package the app
    // in a jar. When doing this the files cannot be accessed in the same way. They can only be retrieved by using input streams.
    private fun readSudokuBoard(): Array<IntArray> {
        val sudokuFile = File("./src/sudoku_1")

        val sudokuArray = Array(9) { IntArray(9) }
        sudokuFile.readLines().forEachIndexed { index, line ->
            sudokuArray[index] = line.split(" ").map { it.toInt() }.toIntArray()
        }

        return sudokuArray
    }

    private fun initVisibleElements(): Array<BooleanArray> {
        return Array(9) { index ->
            BooleanArray(9) { elementIndex -> sudokuBoard[index][elementIndex] != 0 }
        }
    }

    fun isUserInputCorrect(userInput: Int, row: Int, col: Int): Boolean {
        // check number is between 1 and 9
        if (userInput < 0 || userInput > 9) {
            return false
        }

        // check number is not used yet in sudoku board row
        if (sudokuBoard[row].contains(userInput)) {
            return false
        }

        // check number is not used yet in sudoku board column
        (0 until 9)
                .filter { sudokuBoard[it][col] == userInput }
                .forEach { return false }

        // check number is not used yet in sudoku board 3x3 area
        val startRow = row / 3 * 3
        val startColumn = col / 3 * 3
        (startRow until startRow + 3).forEach { i ->
            (startColumn until startColumn + 3)
                    .filter { j -> sudokuBoard[i][j] == userInput }
                    .forEach { return false }
        }

        return true
    }

    fun isSolved(): Boolean {
        visibleElements.forEach { array ->
            array.forEach { element ->
                if (!element) {
                    return false
                }
            }
        }
        return true
    }
}