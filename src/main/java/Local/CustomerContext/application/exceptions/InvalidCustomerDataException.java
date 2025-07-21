package Local.CustomerContext.application.exceptions;

public class InvalidCustomerDataException extends CustomerApplicationException {
    public InvalidCustomerDataException(String message) {
        super("Invalid customer data: " + message);
    }
}
