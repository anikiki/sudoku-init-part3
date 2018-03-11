import org.testng.annotations.Test
import kotlin.test.assertTrue

internal class SudokuGameTest {

    private val sudokuGame = SudokuGame()

    @Test
    fun isUserInputCorrectFor3InRow0Column0() {
        val result = sudokuGame.isUserInputCorrect(3, 0, 0)

        assertTrue { result }
    }

}