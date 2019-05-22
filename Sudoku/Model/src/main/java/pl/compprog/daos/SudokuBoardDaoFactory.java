package pl.compprog.daos;

import pl.compprog.daos.Dao;
import pl.compprog.daos.FileSudokuBoardDao;
import pl.compprog.exceptions.ApplicationException;

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
    public final Dao getFileDao(final String filename) throws ApplicationException {
        return new FileSudokuBoardDao(filename);
    }
}
