package pl.compprog.daos;

import pl.compprog.exceptions.ApplicationException;
import pl.compprog.sudoku.SudokuBoard;

import java.util.ResourceBundle;

public abstract class AbstractDao<T> implements Dao<T>, AutoCloseable {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("pl.compprog.messages");

    public AbstractDao() throws ApplicationException {
        if (bundle == null) {
            throw new ApplicationException(ApplicationException.RESOURCE_BUNDLE_IS_NULL);
        }
    }

    public static String getDaoMessage(String key) {
        return bundle.getString(key);
    }
}