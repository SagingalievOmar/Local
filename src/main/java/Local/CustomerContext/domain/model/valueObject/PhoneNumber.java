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
public class PhoneNumber extends ValueObject {

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[1-9]\\d{1,14}$"
    );

    @Column(length = 32, nullable = false)
    private final String value;

    protected PhoneNumber() {
        this.value = null;
    }

    public PhoneNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessRuleException("Phone number cannot be empty");
        }

        String cleanValue = value.replaceAll("[\\s\\-\\(\\)]", "");

        if (!PHONE_PATTERN.matcher(cleanValue).matches()) {
            throw new BusinessRuleException("Invalid phone number format: " + value);
        }

        this.value = cleanValue;
    }

    public String getFormattedValue() {
        if (value.startsWith("+7")) {
            return String.format("+7 (%s) %s-%s-%s",
                    value.substring(2, 5),
                    value.substring(5, 8),
                    value.substring(8, 10),
                    value.substring(10, 12));
        }
        return value;
    }

    public String getCountryCode() {
        if (value.startsWith("+")) {
            return value.substring(0, 2);
        }
        return "+7";
    }

}