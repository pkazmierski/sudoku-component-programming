package pl.compprog.sudoku;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pl.compprog.difficulties.Difficulty;
import pl.compprog.difficulties.DifficultyNormal;
import pl.compprog.logs.FileAndConsoleLogger;
import pl.compprog.solvers.BacktrackingSudokuSolver;
import pl.compprog.solvers.SudokuSolver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to test whether sudoku board was properly generated.
 */
public class SudokuBoardTest {


    private static final Logger logger = FileAndConsoleLogger.getConfiguredLogger(SudokuBoardTest.class.getName());
    private final int sizeOfSudoku = 9;

    /**
     * Tests whether each value of sudoku board is valid according to sudoku
     * rules
     */
    @Test
    public void testValidnessOfSudoku() {
        SudokuBoard sudokuBoard = new SudokuBoard();
        logger.log(Level.INFO, sudokuBoard.toString());
        SudokuSolver solver = new BacktrackingSudokuSolver();
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
        Difficulty d = new DifficultyNormal();
        d.prepareBoard(sudokuBoard);
        logger.log(Level.INFO, sudokuBoard.toString());
    }
    

    /**
     * Tests whether two subsequent calls of fillBoard generates different
     * digits layout on the board
     */
    @Test
    public void testRandomnessOfSudoku() {
        SudokuBoard sudokuBoard1 = new SudokuBoard();
        SudokuBoard sudokuBoard2 = new SudokuBoard();
        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(sudokuBoard1);
        solver.solve(sudokuBoard2);
        assertNotEquals(sudokuBoard1, sudokuBoard2);
    }


    /**
     * Tests whether cloned sudokuBoard is not the
     * same board as the previous one
     */
    @Test
    public void cloneTest()
    {
        SudokuBoard sudokuBoard1 = new SudokuBoard();
        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(sudokuBoard1);
        SudokuBoard sudokuBoard2 = sudokuBoard1.clone();
        assertNotSame(sudokuBoard1, sudokuBoard2);
        assertNotSame(sudokuBoard1.getFieldAt(0, 0), sudokuBoard2.getFieldAt(0, 0));
        assertEquals(sudokuBoard1, sudokuBoard2);
        assertEquals(sudokuBoard1.getFieldAt(0, 0), sudokuBoard2.getFieldAt(0, 0));
    }

}