package Local.CustomerContext.application.exceptions;

public class CustomerApplicationException extends RuntimeException {
    public CustomerApplicationException(String message) {
        super(message);
    }

    public CustomerApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
