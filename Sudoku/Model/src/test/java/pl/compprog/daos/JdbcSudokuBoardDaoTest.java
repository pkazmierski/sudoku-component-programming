package pl.compprog.daos;

import org.junit.jupiter.api.Test;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.logs.FileAndConsoleLoggerFactory;
import pl.compprog.solvers.BacktrackingSudokuSolver;
import pl.compprog.solvers.SudokuSolver;
import pl.compprog.sudoku.SudokuBoard;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSudokuBoardDaoTest {

    private static final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(JdbcSudokuBoardDao.class.getName());

    @Test
    public void testCreateDaoWithNullName() {
        boolean[][] temp = new boolean[9][9];
        assertThrows(DaoException.class, () -> new JdbcSudokuBoardDao(null, temp));
    }


    @Test
    public void testReadFromNonExistingBoard() {
        boolean[][] temp = new boolean[9][9];
        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("nonExistingBoard!@#$%^&%^%*&^", temp))
        {
            assertThrows(DaoException.class, dao::readEx);
        } catch (ApplicationException | SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void testWriteNullBoard() {
        boolean[][] temp = new boolean[9][9];
        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("nonExistingBoard!@#$%^&%^%*&^", temp))
        {
            assertThrows(DaoException.class, () -> dao.writeEx(null));
        } catch (ApplicationException | SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void readAndWriteBoardTest() {
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
            dao.writeEx(sudokuBoard1);
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

    @Test
    public void displayAllTest() {
        SudokuBoard sudokuBoard1 = new SudokuBoard();
        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(sudokuBoard1);
        boolean[][] wasGenerated1 = new boolean[9][9];
        for (int i = 0; i < wasGenerated1.length; i++) {
            for (int j  = 0; j < wasGenerated1[0].length; j++) {
                wasGenerated1[i][j] = true;
            }
        }
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        try(JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao) sudokuBoardDaoFactory.getDatabaseDao("sudokuBoard", wasGenerated1)) {
            dao.writeEx(sudokuBoard1);
            List<String[]> list = JdbcSudokuBoardDao.getAllBoardsAsStrings();
            assertEquals(1, list.size());
            String[] array = list.get(0);
            assertEquals("sudokuBoard", array[0]);
            logger.log(Level.INFO, array[1]);
            dao.deleteEx();
        }
        catch (ApplicationException | SQLException ex) {
            ex.printStackTrace();
        }
    }

}