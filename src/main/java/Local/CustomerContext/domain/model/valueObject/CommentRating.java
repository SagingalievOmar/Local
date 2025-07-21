package Local.CustomerContext.domain.model.valueObject;

import Local.shared.domain.ValueObject;
import Local.shared.infrastructure.exception.BusinessRuleException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Embeddable
public class CommentRating extends ValueObject {
    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    @Column
    private final int value;

    protected CommentRating() {
        this.value = 0;
    }

    public CommentRating(int value) {
        if (value < MIN_RATING || value > MAX_RATING) {
            throw new BusinessRuleException(
                    String.format("Rating must be between %d and %d", MIN_RATING, MAX_RATING)
            );
        }
        this.value = value;
    }

    public boolean isPositive() {
        return value >= 4;
    }

    public boolean isNegative() {
        return value <= 2;
    }

    public boolean isNeutral() {
        return value == 3;
    }

    public String getDescription() {
        return switch (value) {
            case 1 -> "Очень плохо";
            case 2 -> "Плохо";
            case 3 -> "Нормально";
            case 4 -> "Хорошо";
            case 5 -> "Отлично";
            default -> "Неизвестно";
        };
    }

}