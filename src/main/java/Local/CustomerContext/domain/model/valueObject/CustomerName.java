package Local.CustomerContext.domain.model.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.ToString;
import Local.shared.domain.ValueObject;
import Local.shared.infrastructure.exception.BusinessRuleException;

@Getter
@ToString
@Embeddable
public class CustomerName extends ValueObject {

    private final static int MAX_LENGTH = 64;

    @Column(length = MAX_LENGTH, nullable = false)
    private final String firstName;

    @Column(length = MAX_LENGTH, nullable = false)
    private final String lastName;

    @Column(length = MAX_LENGTH)
    private final String middleName;

    protected CustomerName() {
        this.firstName = null;
        this.lastName = null;
        this.middleName = null;
    }

    public CustomerName(String firstName, String lastName) {
        this(firstName, lastName, null);
    }

    public CustomerName(String firstName, String lastName, String middleName) {
        validateNotEmpty(firstName, "First name cannot be empty");
        validateNotEmpty(lastName, "Last name cannot be empty");
        validateLength(firstName, "First name", 2);
        validateLength(lastName, "Last name", 2);

        if (middleName != null) {
            validateLength(middleName, "Middle name", 1);
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.middleName = middleName != null ? middleName.trim() : null;
    }

    private void validateNotEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessRuleException(message);
        }
    }

    private void validateLength(String value, String fieldName, int min) {
        if (value.trim().length() < min || value.trim().length() > MAX_LENGTH) {
            throw new BusinessRuleException(
                    String.format("%s must be between %d and %d characters", fieldName, min, MAX_LENGTH)
            );
        }
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);

        if (middleName != null && !middleName.isEmpty()) {
            sb.append(" ").append(middleName);
        }

        sb.append(" ").append(lastName);

        return sb.toString();
    }

    public String getShortName() {
        return firstName + " " + lastName;
    }

    public String getInitials() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName.charAt(0)).append(".");

        if (middleName != null && !middleName.isEmpty()) {
            sb.append(middleName.charAt(0)).append(".");
        }

        sb.append(lastName.charAt(0)).append(".");

        return sb.toString();
    }

}