package pl.compprog;

/**
 * Class representing so called factory of FileSudokuBoardDao.
 */
public class SudokuBoardDaoFactory {

    /**
     * Returns new FileSudokuBoardDao object.
     *
     * @param filename  name of processed file
     *@return new FileSudokuBoardDao object
     */
    public final Dao getFileDao(final String filename) {
        return new FileSudokuBoardDao(filename);
    }
}
