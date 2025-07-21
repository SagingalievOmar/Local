package Local.CustomerContext.application.exceptions;

public class CustomerNotFoundException extends CustomerApplicationException {
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with id: " + customerId);
    }

    public CustomerNotFoundException(String email) {
        super("Customer not found with email: " + email);
    }
}
