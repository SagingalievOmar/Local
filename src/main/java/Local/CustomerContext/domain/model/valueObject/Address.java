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
public class Address extends ValueObject {

    private final static int MAX_LENGTH = 64;

    @Column(length = MAX_LENGTH, nullable = false)
    private final String street;

    @Column(length = MAX_LENGTH, nullable = false)
    private final String city;

    @Column(length = MAX_LENGTH)
    private final String state;

    @Column(length = 32, nullable = false)
    private final String postalCode;

    @Column(length = MAX_LENGTH, nullable = false)
    private final String country;

    @Column(length = MAX_LENGTH)
    private final String apartment;

    public Address() {
        this.street = null;
        this.city = null;
        this.state = null;
        this.postalCode = null;
        this.country = null;
        this.apartment = null;
    }

    public Address(String street, String city, String state, String postalCode, String country) {
        this(street, city, state, postalCode, country, null);
    }

    public Address(String street, String city, String state, String postalCode, String country, String apartment) {
        validateNotEmpty(street, "Street cannot be empty");
        validateNotEmpty(city, "City cannot be empty");
        validateNotEmpty(postalCode, "Postal code cannot be empty");
        validateNotEmpty(country, "Country cannot be empty");

        this.street = street.trim();
        this.city = city.trim();
        this.state = state != null ? state.trim() : null;
        this.postalCode = postalCode.trim();
        this.country = country.trim();
        this.apartment = apartment != null ? apartment.trim() : null;
    }

    private void validateNotEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessRuleException(message);
        }
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street);

        if (apartment != null && !apartment.isEmpty()) {
            sb.append(", ").append(apartment);
        }

        sb.append(", ").append(city);

        if (state != null && !state.isEmpty()) {
            sb.append(", ").append(state);
        }

        sb.append(" ").append(postalCode);
        sb.append(", ").append(country);

        return sb.toString();
    }

    public boolean isInCountry(String countryName) {
        return country.equalsIgnoreCase(countryName);
    }

}
