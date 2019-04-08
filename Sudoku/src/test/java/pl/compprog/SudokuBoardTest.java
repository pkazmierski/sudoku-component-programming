package pl.compprog;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Class used to test whether sudoku board was properly generated.
 */
public class SudokuBoardTest {

    private final int sizeOfSudoku = 9;

    /**
     * Tests whether each value of sudoku board is valid according to sudoku
     * rules
     */
    @Test
    public void testValidnessOfSudoku() {
        SudokuBoard sudokuBoard = new SudokuBoard();
        System.out.println(sudokuBoard);
        SudokuSolver solver = new BactrackingSudokuSolver();
        solver.solve(sudokuBoard);
        for (int y = 0; y < sizeOfSudoku; y++) {
            assertTrue(sudokuBoard.getRow(y).verify());
        }

        for (int x = 0; x < sizeOfSudoku; x++) {
            assertTrue(sudokuBoard.getRow(x).verify());
        }

        for (int y = 0; y < sizeOfSudoku; y++) {
            for (int x = 0; x < sizeOfSudoku; x++) {
                assertTrue(sudokuBoard.getBox(x, y).verify());
            }
        }
        System.out.println(sudokuBoard);
    }
    

    /**
     * Tests whether two subsequent calls of fillBoard generates different
     * digits layout on the board
     */
    @Test
    public void testRandomnessOfSudoku() {
        SudokuBoard sudokuBoard1 = new SudokuBoard();
        SudokuBoard sudokuBoard2 = new SudokuBoard();
        SudokuSolver solver = new BactrackingSudokuSolver();
        solver.solve(sudokuBoard1);
        solver.solve(sudokuBoard2);
        assertNotEquals(sudokuBoard1, sudokuBoard2);
    }
}