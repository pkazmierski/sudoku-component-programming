package pl.compprog;

import org.junit.jupiter.api.Test;
import pl.compprog.daos.FileSudokuBoardDao;
import pl.compprog.daos.SudokuBoardDaoFactory;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.logs.LogConfigurator;
import pl.compprog.solvers.BacktrackingSudokuSolver;
import pl.compprog.solvers.SudokuSolver;
import pl.compprog.sudoku.SudokuBoard;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class FileSudokuBoardDaoTest {

    LogConfigurator lc = new LogConfigurator();
    private static final Logger logger = Logger.getLogger(FileSudokuBoardDaoTest.class.getName());

    @Test
    public void testOpenWithoutDeclaredExceptions() {

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
}