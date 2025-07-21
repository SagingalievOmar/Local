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
public class Quantity extends ValueObject {

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 999;

    @Column(nullable = false)
    private final int value;

    protected Quantity() {
        this.value = 0;
    }

    public Quantity(int value) {
        if (value < MIN_QUANTITY) {
            throw new BusinessRuleException(
                    String.format("Quantity cannot be less than %d", MIN_QUANTITY)
            );
        }

        if (value > MAX_QUANTITY) {
            throw new BusinessRuleException(
                    String.format("Quantity cannot be greater than %d", MAX_QUANTITY)
            );
        }

        this.value = value;
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }

    public Quantity subtract(Quantity other) {
        return new Quantity(this.value - other.value);
    }

    public Quantity multiply(int multiplier) {
        return new Quantity(this.value * multiplier);
    }

    public boolean isGreaterThan(Quantity other) {
        return this.value > other.value;
    }

    public boolean isLessThan(Quantity other) {
        return this.value < other.value;
    }

    public boolean isEqualTo(Quantity other) {
        return this.value == other.value;
    }

    public boolean isMaximumReached() {
        return this.value >= MAX_QUANTITY;
    }

    public boolean isMinimumReached() {
        return this.value <= MIN_QUANTITY;
    }

    public static Quantity zero() {
        return new Quantity(MIN_QUANTITY);
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }

}
