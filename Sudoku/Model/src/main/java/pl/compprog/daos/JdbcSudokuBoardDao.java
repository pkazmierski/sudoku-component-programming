package pl.compprog.daos;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.logs.FileAndConsoleLoggerFactory;
import pl.compprog.sudoku.SudokuBoard;
import static java.util.logging.Level.SEVERE;

public class JdbcSudokuBoardDao extends AbstractDao<SudokuBoard>{

    private static final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(JdbcSudokuBoardDao.class.getName());
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:SudokuBoards.db";

    private static final String CREATION_QUERY_FIRST_TABLE = "CREATE TABLE IF NOT EXISTS BOARDS (" +
            "[name] VARCHAR(255) PRIMARY KEY," +
            "[creationDate] VARCHAR(255)" +
            ")";
    private static final String CREATION_QUERY_SECOND_TABLE = "CREATE TABLE IF NOT EXISTS FIELDS (" +
            "[boardName] VARCHAR(255)," +
            "[x] INTEGER," +
            "[y] INTEGER," +
            "[value] INTEGER," +
            "[wasGenerated] INTEGER," +
            "FOREIGN KEY(boardName) REFERENCES BOARDS(name)," +
            "CONSTRAINT pk_field PRIMARY KEY (boardName, x, y)" +
            ")";

    private static final String READ_ALL_BOARDS = "SELECT * FROM BOARDS";
    private static final String READ_QUERY_FIELD = "SELECT * FROM FIELDS WHERE [boardName]=?";
    private static final String WRITE_QUERY_BOARD = "INSERT INTO BOARDS([name], [creationDate]) VALUES(?, strftime('%d/%m/%Y %H:%M:%S', 'now', 'localtime'))";
    private static final String WRITE_QUERY_FIELD = "INSERT INTO FIELDS([boardName], [x], [y], [value], [wasGenerated]) VALUES(?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY_BOARD = "DELETE FROM BOARDS WHERE [name]=?";
    private static final String DELETE_QUERY_FIELDS = "DELETE FROM FIELDS WHERE [boardName]=?";

    private Connection conn;
    private Statement stat;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private String boardName;


    static {
        try {
            Class.forName(DRIVER);
        } catch(ClassNotFoundException cnfex) {
            logger.log(SEVERE, getDaoMessage(DaoException.NO_JDBC_DRIVER), cnfex);
        }
    }


    public JdbcSudokuBoardDao(final String boardName, boolean[][] wasGenerated) throws ApplicationException {
        if (boardName == null) {
            throw new DaoException(DaoException.NULL_NAME);
        }
        if (wasGenerated == null) {
            throw new DaoException(DaoException.NULL_BOARD);
        }
        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
            stat.execute(CREATION_QUERY_FIRST_TABLE);
            stat = conn.createStatement();
            stat.execute(CREATION_QUERY_SECOND_TABLE);
        } catch (SQLException se) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
        this.boardName = boardName;
        this.wasGenerated = wasGenerated;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void close() throws SQLException {
        conn.close();
        stat.close();
        if (pstmt != null) {
            pstmt.close();
        }
        if (rs != null) {
            rs.close();
        }
    }

    @Override
    public final void finalize() throws Exception {
        close();
    }


    public static List<String[]> getAllBoardsAsStrings() throws DaoException {
        try  {
            List<String[]> list = new ArrayList<>();
            JdbcSudokuBoardDao jdbcSudokuBoardDao = new JdbcSudokuBoardDao("temp", new boolean[0][0]);
            jdbcSudokuBoardDao.pstmt = jdbcSudokuBoardDao.conn.prepareStatement(READ_ALL_BOARDS);
            jdbcSudokuBoardDao.rs = jdbcSudokuBoardDao.pstmt.executeQuery();
            while (jdbcSudokuBoardDao.rs.next()) {
                String[] array = new String[2];
                array[0] = jdbcSudokuBoardDao.rs.getString(1);
                array[1] = jdbcSudokuBoardDao.rs.getString(2);
                list.add(array);
            }
            return Collections.unmodifiableList(list);
        } catch (SQLException | ApplicationException e) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
    }


    @SuppressWarnings("Duplicates")
    @Override
    public SudokuBoard readEx() throws DaoException {
        try  {
            SudokuBoard sudokuBoard = new SudokuBoard();
            pstmt = conn.prepareStatement(READ_QUERY_FIELD);
            pstmt.setString(1, boardName);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                int x = rs.getInt(2);
                int y = rs.getInt(3);
                sudokuBoard.unsafeSet(x, y, rs.getInt(4));
                wasGenerated[y][x] = rs.getInt(5) == 1;
            }
            return sudokuBoard;
        } catch (SQLException se) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public SudokuBoard read() {
        try  {
            SudokuBoard sudokuBoard = new SudokuBoard();
            for (int x = 0; x < SudokuBoard.SIZE_OF_SUDOKU; x++) {
                for (int y = 0; y < SudokuBoard.SIZE_OF_SUDOKU; y++) {
                    pstmt = conn.prepareStatement(READ_QUERY_FIELD);
                    pstmt.setString(1, boardName);
                    pstmt.setInt(2, x);
                    pstmt.setInt(3, y);
                    rs = pstmt.executeQuery();
                    sudokuBoard.unsafeSet(x, y, rs.getInt(4));
                    wasGenerated[y][x] = rs.getInt(5) == 1;
                }
            }
            return sudokuBoard;
        } catch (SQLException se) {
            logger.log(SEVERE, getDaoMessage(DaoException.SQL_ERROR), se);
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void writeEx(SudokuBoard sudokuBoard) throws DaoException {
        if (sudokuBoard == null) {
            throw new DaoException(DaoException.NULL_BOARD);
        }
        try  {
            pstmt = conn.prepareStatement(WRITE_QUERY_BOARD);
            pstmt.setString(1, boardName);
            pstmt.execute();
            for (int x = 0; x < SudokuBoard.SIZE_OF_SUDOKU; x++) {
                for (int y = 0; y < SudokuBoard.SIZE_OF_SUDOKU; y++) {
                    pstmt = conn.prepareStatement(WRITE_QUERY_FIELD);
                    pstmt.setString(1, boardName);
                    pstmt.setInt(2, x);
                    pstmt.setInt(3, y);
                    pstmt.setInt(4, sudokuBoard.get(x, y));
                    pstmt.setInt(5, wasGenerated[y][x] ? 1 : 0);
                    pstmt.execute();
                }
            }
        } catch (SQLException se) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void write(SudokuBoard sudokuBoard) {
        if (sudokuBoard == null) {
            logger.log(SEVERE, getDaoMessage(DaoException.NULL_BOARD));
            return;
        }
        try  {
            pstmt = conn.prepareStatement(WRITE_QUERY_BOARD);
            pstmt.setString(1, boardName);
            pstmt.execute();
            for (int x = 0; x < SudokuBoard.SIZE_OF_SUDOKU; x++) {
                for (int y = 0; y < SudokuBoard.SIZE_OF_SUDOKU; y++) {
                    pstmt = conn.prepareStatement(WRITE_QUERY_FIELD);
                    pstmt.setString(1, boardName);
                    pstmt.setInt(2, x);
                    pstmt.setInt(3, y);
                    pstmt.setInt(4, sudokuBoard.get(x, y));
                    pstmt.setInt(5, wasGenerated[y][x] ? 1 : 0);
                    pstmt.execute();
                }
            }
        } catch (SQLException se) {
            logger.log(SEVERE, getDaoMessage(DaoException.SQL_ERROR), se);
        }
    }

    @SuppressWarnings("Duplicates")
    public void delete() {
        try {
            pstmt = conn.prepareStatement(DELETE_QUERY_BOARD);
            pstmt.setString(1, boardName);
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(DELETE_QUERY_FIELDS);
            pstmt.setString(1, boardName);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            logger.log(SEVERE, getDaoMessage(DaoException.SQL_ERROR), se);
        }
    }

    @SuppressWarnings("Duplicates")
    public void deleteEx() throws DaoException {
        try {
            pstmt = conn.prepareStatement(DELETE_QUERY_BOARD);
            pstmt.setString(1, boardName);
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(DELETE_QUERY_FIELDS);
            pstmt.setString(1, boardName);
            pstmt.executeUpdate();

        } catch (SQLException se) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
    }
}
