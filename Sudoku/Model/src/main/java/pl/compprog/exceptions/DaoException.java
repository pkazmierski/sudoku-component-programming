package pl.compprog.exceptions;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DaoException extends ApplicationException {

    private static final ResourceBundle messages;
    //Message keys
    public static final String NULL_NAME = "null.name";
    public static final String NULL_FILE = "null.file";
    public static final String OPEN_ERROR = "open.error";
    public static final String INVALID_CAST = "invalid.cast";
    public static final String NULL_BOARD = "null.board";
    public static final String SQL_ERROR = "sql.error";
    public static final String NO_JDBC_DRIVER = "no.JDBC.driver";
    public static final String NO_SUCH_RECORD = "no.such.record";

    static {
        Locale locale = Locale.getDefault(Locale.Category.DISPLAY);
        messages = ResourceBundle.getBundle("pl.compprog.messages", locale);
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getLocalizedMessage() {
        String message;
        try {
            message = messages.getString(getMessage());
        } catch (MissingResourceException mre) {
            message = "No resource for " + getMessage() + "key";
        }
        return message;
    }

}
