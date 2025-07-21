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
public class CommentText extends ValueObject {
    private static final int MAX_LENGTH = 1000;

    @Column(columnDefinition = "TEXT")
    private final String content;

    protected CommentText() {
        this.content = null;
    }

    public CommentText(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessRuleException("Comment text cannot be empty");
        }

        String trimmed = content.trim();

        if (trimmed.length() > MAX_LENGTH) {
            throw new BusinessRuleException("Comment text is too long. Maximum length is " + MAX_LENGTH + " characters");
        }

        this.content = trimmed;
    }

    public int getLength() {
        return content != null ? content.length() : 0;
    }

    public String getPreview(int maxLength) {
        if (content == null || content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }

}