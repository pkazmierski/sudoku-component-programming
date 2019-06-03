package pl.compprog.daos;

import org.junit.jupiter.api.Test;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.logs.FileAndConsoleLoggerFactory;
import pl.compprog.solvers.BacktrackingSudokuSolver;
import pl.compprog.solvers.SudokuSolver;
import pl.compprog.sudoku.SudokuBoard;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class FileSudokuBoardDaoTest {

    private static final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(FileSudokuBoardDaoTest.class.getName());

    @Test
    public void testCreateDaoWithNullName() {

        assertThrows(DaoException.class, () -> new FileSudokuBoardDao(null));
    }


    @Test
    public void testWriteToNonExistingFile() {
        try (FileSudokuBoardDao dao = new FileSudokuBoardDao("nonExistingFile!@#$%^&%^%*&^"))
        {
            SudokuBoard sudokuBoard = new SudokuBoard();
            assertThrows(DaoException.class, () -> dao.writeEx(sudokuBoard));
        } catch (ApplicationException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testReadFromNonExistingFile() {
        try (FileSudokuBoardDao dao = new FileSudokuBoardDao("nonExistingFile!@#$%^&%^%*&^"))
        {
            assertThrows(DaoException.class, dao::readEx);
        } catch (ApplicationException | IOException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void testWriteNullBoard() {
        try (FileSudokuBoardDao dao = new FileSudokuBoardDao("nonExistingFile!@#$%^&%^%*&^"))
        {
            assertThrows(DaoException.class, () -> dao.writeEx(null));
        } catch (ApplicationException | IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Tests whether sudokuBoard written to and read from the file
     * is the same
     */
    @Test
    public void readAndWriteFileTest()
    {
        SudokuBoard sudokuBoard1 = new SudokuBoard();
        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(sudokuBoard1);
        logger.log(Level.INFO, sudokuBoard1.toString());
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        try(FileSudokuBoardDao dao = (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao("sudoku.ser")) {
            dao.writeEx(sudokuBoard1);
            SudokuBoard sudokuBoard2 = dao.readEx();
            assertEquals(sudokuBoard1, sudokuBoard2);
        }
        catch (ApplicationException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readAndWriteFileWithWasGeneratedTest()
    {
        SudokuBoard sudokuBoard1 = new SudokuBoard();
        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(sudokuBoard1);
        boolean[][] wasGenerated1 = new boolean[9][9];
        for (int i = 0; i < wasGenerated1.length; i++) {
            for (int j  = 0; j < wasGenerated1[0].length; j++) {
                wasGenerated1[i][j] = true;
            }
        }
        logger.log(Level.INFO, sudokuBoard1.toString());
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        try(FileSudokuBoardDao dao = (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao("sudoku.ser", wasGenerated1)) {
            dao.write(sudokuBoard1);
            SudokuBoard sudokuBoard2 = dao.readEx();
            boolean[][] wasGenerated2 = dao.getWasGenerated();
            assertEquals(sudokuBoard1, sudokuBoard2);
            assertEquals(wasGenerated1.length, wasGenerated2.length);
            for (int i = 0; i < wasGenerated1.length; i++) {
                assertArrayEquals(wasGenerated1[i], wasGenerated2[i]);
            }
        }
        catch (ApplicationException | IOException ex) {
            ex.printStackTrace();
        }
    }
}