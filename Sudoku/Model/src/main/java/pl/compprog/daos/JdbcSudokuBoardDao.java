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
    private static final String CREATION_QUERY_WITH_WAS_GENERATED_BOARD = "CREATE TABLE IF NOT EXISTS BOARDS_WITH_WAS_GENERATED_BOARD ([name] VARCHAR(255) PRIMARY KEY, [content] LONGVARBINARY(1000000), [wasGenerated] LONGVARBINARY(8100))";
    private static final String READ_QUERY = "SELECT * FROM BOARDS WHERE [name]=?";
    private static final String READ_QUERY_WITH_WAS_GENERATED_BOARD = "SELECT * FROM BOARDS_WITH_WAS_GENERATED_BOARD WHERE [name]=?";
    private static final String WRITE_QUERY = "INSERT INTO BOARDS([name], [content]) VALUES(?, ?)";
    private static final String WRITE_QUERY_WITH_WAS_GENERATED_BOARD = "INSERT INTO BOARDS_WITH_WAS_GENERATED_BOARD([name], [content], [wasGenerated]) VALUES(?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM BOARDS WHERE [name]=?";
    private static final String DELETE_QUERY_WITH_WAS_GENERATED_BOARD = "DELETE FROM BOARDS_WITH_WAS_GENERATED_BOARD WHERE [name]=?";


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
            stat.execute(CREATION_QUERY_WITH_WAS_GENERATED_BOARD);
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
    }

    @Override
    public final void finalize() throws Exception {
        close();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public SudokuBoard readEx() throws DaoException {
        try  {
            SudokuBoard sudokuBoard;
            PreparedStatement pstmt;
            byte[] st;
            if (wasGenerated == null) {
                pstmt = conn.prepareStatement(READ_QUERY);
                pstmt.setString(1, boardName);
                ResultSet rs = pstmt.executeQuery();
                st = (byte[]) rs.getObject(2);
                rs.close();
                ByteArrayInputStream  bais = new ByteArrayInputStream(st);
                ObjectInputStream  ois = new ObjectInputStream(bais);
                pstmt.close();
                sudokuBoard = (SudokuBoard) ois.readObject();
                bais.close();
                ois.close();
            } else {
                pstmt = conn.prepareStatement(READ_QUERY_WITH_WAS_GENERATED_BOARD);
                pstmt.setString(1, boardName);
                ResultSet rs = pstmt.executeQuery();
                st = (byte[]) rs.getObject(2);
                byte[] wasGeneratedByte = (byte[]) rs.getObject(3);
                rs.close();
                ByteArrayInputStream  bais1 = new ByteArrayInputStream(st);
                ObjectInputStream  ois1 = new ObjectInputStream(bais1);
                ByteArrayInputStream  bais2 = new ByteArrayInputStream(wasGeneratedByte);
                ObjectInputStream  ois2 = new ObjectInputStream(bais2);
                pstmt.close();
                wasGenerated = (boolean[][]) ois2.readObject();
                sudokuBoard = (SudokuBoard) ois1.readObject();
                bais1.close();
                bais2.close();
                ois1.close();
                ois2.close();
            }
            return sudokuBoard;
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
        try  {
            SudokuBoard sudokuBoard;
            PreparedStatement pstmt;
            byte[] st;
            if (wasGenerated == null) {
                pstmt = conn.prepareStatement(READ_QUERY);
                pstmt.setString(1, boardName);
                ResultSet rs = pstmt.executeQuery();
                st = (byte[]) rs.getObject(2);
                rs.close();
                ByteArrayInputStream  bais = new ByteArrayInputStream(st);
                ObjectInputStream  ois = new ObjectInputStream(bais);
                pstmt.close();
                sudokuBoard = (SudokuBoard) ois.readObject();
                bais.close();
                ois.close();
            } else {
                pstmt = conn.prepareStatement(READ_QUERY_WITH_WAS_GENERATED_BOARD);
                pstmt.setString(1, boardName);
                ResultSet rs = pstmt.executeQuery();
                st = (byte[]) rs.getObject(2);
                byte[] wasGeneratedByte = (byte[]) rs.getObject(3);
                rs.close();
                ByteArrayInputStream  bais1 = new ByteArrayInputStream(st);
                ObjectInputStream  ois1 = new ObjectInputStream(bais1);
                ByteArrayInputStream  bais2 = new ByteArrayInputStream(wasGeneratedByte);
                ObjectInputStream  ois2 = new ObjectInputStream(bais2);
                pstmt.close();
                wasGenerated = (boolean[][]) ois2.readObject();
                sudokuBoard = (SudokuBoard) ois1.readObject();
                bais1.close();
                bais2.close();
                ois1.close();
                ois2.close();
            }
            return sudokuBoard;
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
        try  {
            PreparedStatement pstmt;
            if (wasGenerated == null) {
                pstmt = conn.prepareStatement(WRITE_QUERY);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(sudokuBoard);
                byte[] sudokuBoardAsBytes = baos.toByteArray();
                pstmt.setString(1, boardName);
                pstmt.setBytes(2, sudokuBoardAsBytes);
                pstmt.executeUpdate();
                baos.close();
                oos.close();
            } else {
                pstmt = conn.prepareStatement(WRITE_QUERY_WITH_WAS_GENERATED_BOARD);
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                ObjectOutputStream oos1 = new ObjectOutputStream(baos1);
                oos1.writeObject(sudokuBoard);
                byte[] sudokuBoardAsBytes = baos1.toByteArray();
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
                oos2.writeObject(wasGenerated);
                byte[] wasGeneratedAsBytes = baos2.toByteArray();
                pstmt.setString(1, boardName);
                pstmt.setBytes(2, sudokuBoardAsBytes);
                pstmt.setBytes(3, wasGeneratedAsBytes);
                pstmt.executeUpdate();
                oos1.close();
                oos2.close();
                baos1.close();
                baos2.close();
            }
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
        try  {
            PreparedStatement pstmt;
            if (wasGenerated == null) {
                pstmt = conn.prepareStatement(WRITE_QUERY);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(sudokuBoard);
                byte[] sudokuBoardAsBytes = baos.toByteArray();
                pstmt.setString(1, boardName);
                pstmt.setBytes(2, sudokuBoardAsBytes);
                pstmt.executeUpdate();
                baos.close();
                oos.close();
            } else {
                pstmt = conn.prepareStatement(WRITE_QUERY_WITH_WAS_GENERATED_BOARD);
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                ObjectOutputStream oos1 = new ObjectOutputStream(baos1);
                oos1.writeObject(sudokuBoard);
                byte[] sudokuBoardAsBytes = baos1.toByteArray();
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
                oos2.writeObject(wasGenerated);
                byte[] wasGeneratedAsBytes = baos2.toByteArray();
                pstmt.setString(1, boardName);
                pstmt.setBytes(2, sudokuBoardAsBytes);
                pstmt.setBytes(3, wasGeneratedAsBytes);
                pstmt.executeUpdate();
                oos1.close();
                oos2.close();
                baos1.close();
                baos2.close();
            }
        } catch (SQLException se) {
            logger.log(SEVERE, getDaoMessage(DaoException.SQL_ERROR), se);
        } catch (IOException ioex) {
            logger.log(SEVERE, getDaoMessage(DaoException.OPEN_ERROR), ioex);
        }
    }

    @SuppressWarnings("Duplicates")
    public void delete() {
        try {
            PreparedStatement stmt;
            if (wasGenerated == null) {
                stmt = conn.prepareStatement(DELETE_QUERY);

            } else {
                stmt = conn.prepareStatement(DELETE_QUERY_WITH_WAS_GENERATED_BOARD);
            }
            stmt.setString(1, boardName);
            stmt.executeUpdate();

        } catch (SQLException se) {
            logger.log(SEVERE, getDaoMessage(DaoException.SQL_ERROR), se);
        }
    }

    @SuppressWarnings("Duplicates")
    public void deleteEx() throws DaoException {
        try {
            PreparedStatement stmt;
            if (wasGenerated == null) {
                stmt = conn.prepareStatement(DELETE_QUERY);

            } else {
                stmt = conn.prepareStatement(DELETE_QUERY_WITH_WAS_GENERATED_BOARD);
            }
            stmt.setString(1, boardName);
            stmt.executeUpdate();

        } catch (SQLException se) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
    }
}
