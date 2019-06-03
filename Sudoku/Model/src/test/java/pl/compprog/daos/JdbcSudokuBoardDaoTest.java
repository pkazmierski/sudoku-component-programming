package pl.compprog.daos;

import org.junit.jupiter.api.Test;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.logs.FileAndConsoleLoggerFactory;
import pl.compprog.solvers.BacktrackingSudokuSolver;
import pl.compprog.solvers.SudokuSolver;
import pl.compprog.sudoku.SudokuBoard;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSudokuBoardDaoTest {

    private static final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(JdbcSudokuBoardDao.class.getName());

    @Test
    public void testCreateDaoWithNullName() {

        assertThrows(DaoException.class, () -> new JdbcSudokuBoardDao(null));
    }


    @Test
    public void testReadFromNonExistingBoard() {
        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("nonExistingBoard!@#$%^&%^%*&^"))
        {
            assertThrows(DaoException.class, dao::readEx);
        } catch (ApplicationException | SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void testWriteNullBoard() {
        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("nonExistingBoard!@#$%^&%^%*&^"))
        {
            assertThrows(DaoException.class, () -> dao.writeEx(null));
        } catch (ApplicationException | SQLException ex) {
            ex.printStackTrace();
        }
    }



    @Test
    public void readAndWriteTest() {
        SudokuBoard sudokuBoard1 = new SudokuBoard();
        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(sudokuBoard1);
        logger.log(Level.INFO, sudokuBoard1.toString());
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        try(JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao) sudokuBoardDaoFactory.getDatabaseDao("sudokuBoard")) {
            dao.writeEx(sudokuBoard1);
            SudokuBoard sudokuBoard2 = dao.readEx();
            logger.log(Level.INFO, sudokuBoard2.toString());
            assertEquals(sudokuBoard1, sudokuBoard2);
            dao.deleteEx();
        }
        catch (ApplicationException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readAndWriteWithWasGeneratedBoardTest() {
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
        try(JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao) sudokuBoardDaoFactory.getDatabaseDao("sudokuBoard", wasGenerated1)) {
            SudokuBoard sudokuBoard2 = dao.readEx();
            boolean[][] wasGenerated2 = dao.getWasGenerated();
            assertEquals(sudokuBoard1, sudokuBoard2);
            assertEquals(wasGenerated1.length, wasGenerated2.length);
            for (int i = 0; i < wasGenerated1.length; i++) {
                assertArrayEquals(wasGenerated1[i], wasGenerated2[i]);
            }
            dao.deleteEx();
        }
        catch (ApplicationException | SQLException ex) {
            ex.printStackTrace();
        }
    }

}