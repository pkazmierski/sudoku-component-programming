package pl.compprog.exceptions;

public class ApplicationException extends Exception {

    public static final String RESOURCE_BUNDLE_IS_NULL = "null.resource";


    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }


}
