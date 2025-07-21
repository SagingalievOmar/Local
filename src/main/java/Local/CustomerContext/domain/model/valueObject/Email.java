package Local.CustomerContext.domain.model.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.ToString;
import Local.shared.domain.ValueObject;
import Local.shared.infrastructure.exception.BusinessRuleException;

import java.util.regex.Pattern;

@Getter
@ToString
@Embeddable
public class Email extends ValueObject {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @Column(length = 128, nullable = false, unique = true)
    private final String email;

    protected Email() {
        this.email = null;
    }

    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessRuleException("Email cannot be empty");
        }

        String trimmedValue = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(trimmedValue).matches()) {
            throw new BusinessRuleException("Invalid email format: " + value);
        }

        this.email = trimmedValue;
    }

    public String getDomain() {
        return email.substring(email.indexOf('@') + 1);
    }

    public String getLocalPart() {
        return email.substring(0, email.indexOf('@'));
    }

}
