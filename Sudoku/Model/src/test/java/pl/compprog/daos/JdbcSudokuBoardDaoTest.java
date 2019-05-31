package pl.compprog.daos;

import org.junit.jupiter.api.Test;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.logs.FileAndConsoleLoggerFactory;
import pl.compprog.solvers.BacktrackingSudokuSolver;
import pl.compprog.solvers.SudokuSolver;
import pl.compprog.sudoku.SudokuBoard;
import java.io.IOException;
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
        } catch (ApplicationException | IOException | SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void testWriteNullBoard() {
        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("nonExistingBoard!@#$%^&%^%*&^"))
        {
            assertThrows(DaoException.class, () -> dao.writeEx(null));
        } catch (ApplicationException | IOException | SQLException ex) {
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
        catch (ApplicationException | SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

}