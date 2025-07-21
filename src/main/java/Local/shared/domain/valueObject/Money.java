package Local.shared.domain.valueObject;

import lombok.Getter;
import lombok.ToString;
import Local.shared.domain.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@ToString
public class Money extends ValueObject {

    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.toUpperCase();
    }

    // Фабричные методы
    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    public static Money of(double amount, String currency) {
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount), "RUB");
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    // Методы сравнения
    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isEqualTo(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) == 0;
    }

    public boolean isGreaterThanOrEqualTo(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    // Арифметические операции
    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier), this.currency);
    }

    public Money multiply(double multiplier) {
        return multiply(BigDecimal.valueOf(multiplier));
    }

    public Money divide(BigDecimal divisor) {
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Money(this.amount.divide(divisor, 2, RoundingMode.HALF_UP), this.currency);
    }

    public Money divide(double divisor) {
        return divide(BigDecimal.valueOf(divisor));
    }

    public BigDecimal divide(long divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return this.amount.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
    }

    public Money negate() {
        return new Money(this.amount.negate(), this.currency);
    }

    public Money abs() {
        return new Money(this.amount.abs(), this.currency);
    }

    // Утилитные методы
    public int intValue() {
        return amount.intValue();
    }

    public long longValue() {
        return amount.longValue();
    }

    public double doubleValue() {
        return amount.doubleValue();
    }

    private void validateSameCurrency(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare with null money");
        }
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    String.format("Cannot operate with different currencies: %s and %s",
                            this.currency, other.currency));
        }
    }
}