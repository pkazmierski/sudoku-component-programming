package pl.compprog.daos;
import java.io.*;
import java.sql.*;
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

    private static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS BOARDS ([name] VARCHAR(255) PRIMARY KEY, [content] LONGVARBINARY(1000000))";
    private static final String READ_QUERY = "SELECT * FROM BOARDS WHERE [name]=?";
    private static final String WRITE_QUERY = "INSERT INTO BOARDS([name], [content]) VALUES(?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM BOARDS WHERE [name]=?";

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    private ByteArrayInputStream bais;
    private ObjectInputStream ois;

    private Connection conn;
    private Statement stat;
    private String boardName;

    static {
        try {
            Class.forName(DRIVER);
        } catch(ClassNotFoundException cnfex) {
            logger.log(SEVERE, getDaoMessage(DaoException.NO_JDBC_DRIVER), cnfex);
        }
    }

    public JdbcSudokuBoardDao(final String boardName) throws ApplicationException {
        if (boardName == null) {
            throw new DaoException(DaoException.NULL_NAME);
        }
        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
            stat.execute(CREATION_QUERY);
        } catch (SQLException se) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
        this.boardName = boardName;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void close() throws SQLException, IOException {
        conn.close();
        stat.close();
        if (baos != null) {
            baos.close();
            oos.close();
        }
        if (bais != null) {
            bais.close();
            ois.close();
        }
    }

    @Override
    public final void finalize() throws Exception {
        close();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public SudokuBoard readEx() throws DaoException {
        try (PreparedStatement pstmt = conn
                .prepareStatement(READ_QUERY)) {
            pstmt.setString(1, boardName);
            ResultSet rs = pstmt.executeQuery();
            byte[] st = (byte[]) rs.getObject(2);

            if (bais == null) {
                bais = new ByteArrayInputStream(st);
                ois = new ObjectInputStream(bais);
            }
            rs.close();
            return (SudokuBoard) ois.readObject();

        } catch (SQLException se) {
            se.printStackTrace();
            throw new DaoException(DaoException.SQL_ERROR);
        } catch (IOException ioex) {
            throw new DaoException(DaoException.OPEN_ERROR);
        } catch (ClassNotFoundException cnfex) {
            throw new DaoException(DaoException.INVALID_CAST);
        } catch (NullPointerException npex) {
            throw new DaoException(DaoException.NO_SUCH_RECORD);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public SudokuBoard read() {
        try (PreparedStatement pstmt = conn
                .prepareStatement(READ_QUERY)) {
            pstmt.setString(1, boardName);
            ResultSet rs = pstmt.executeQuery();
            byte[] st = (byte[]) rs.getObject(1);
            if (bais == null) {
                bais = new ByteArrayInputStream(st);
                ois = new ObjectInputStream(bais);
            }
            rs.close();
            return (SudokuBoard) ois.readObject();
        } catch (SQLException se) {
            logger.log(SEVERE, getDaoMessage(DaoException.SQL_ERROR), se);
        } catch (IOException ioex) {
            logger.log(SEVERE, getDaoMessage(DaoException.OPEN_ERROR), ioex);
        } catch (ClassNotFoundException cnfex) {
            logger.log(SEVERE, getDaoMessage(DaoException.INVALID_CAST), cnfex);
        } catch (NullPointerException npex) {
            logger.log(SEVERE, getDaoMessage(DaoException.NO_SUCH_RECORD), npex);
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void writeEx(SudokuBoard sudokuBoard) throws DaoException {
        if (sudokuBoard == null) {
            throw new DaoException(DaoException.NULL_BOARD);
        }
        try (PreparedStatement pstmt = conn
                .prepareStatement(WRITE_QUERY)) {
            if (baos == null) {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
            }
            oos.writeObject(sudokuBoard);
            byte[] sudokuBoardAsBytes = baos.toByteArray();
            pstmt.setString(1, boardName);
            pstmt.setBytes(2, sudokuBoardAsBytes);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
            throw new DaoException(DaoException.SQL_ERROR);
        } catch (IOException ioex) {
            throw new DaoException(DaoException.OPEN_ERROR);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void write(SudokuBoard sudokuBoard) {
        if (sudokuBoard == null) {
            logger.log(SEVERE, getDaoMessage(DaoException.NULL_BOARD));
            return;
        }
        try (PreparedStatement pstmt = conn
                .prepareStatement(WRITE_QUERY)) {
            if (baos == null) {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
            }
            oos.writeObject(sudokuBoard);
            byte[] sudokuBoardAsBytes = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(sudokuBoardAsBytes);
            pstmt.setString(1, boardName);
            pstmt.setBinaryStream(2, bais, sudokuBoardAsBytes.length);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            logger.log(SEVERE, getDaoMessage(DaoException.SQL_ERROR), se);
        } catch (IOException ioex) {
            logger.log(SEVERE, getDaoMessage(DaoException.OPEN_ERROR), ioex);
        }
    }

    @SuppressWarnings("Duplicates")
    public void delete() {
        try (PreparedStatement stmt=
                     conn.prepareStatement(DELETE_QUERY)  ) {
            stmt.setString(1, boardName);
            stmt.executeUpdate();
        } catch (SQLException se) {
            logger.log(SEVERE, getDaoMessage(DaoException.SQL_ERROR), se);
        }
    }

    @SuppressWarnings("Duplicates")
    public void deleteEx() throws DaoException {
        try (PreparedStatement stmt=
                     conn.prepareStatement(DELETE_QUERY)  ) {
            stmt.setString(1, boardName);
            stmt.executeUpdate();
        } catch (SQLException se) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
    }
}
