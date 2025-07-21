package Local.CustomerContext.domain.model;

import Local.CustomerContext.domain.model.valueObject.CommentRating;
import Local.CustomerContext.domain.model.valueObject.CommentText;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import Local.shared.domain.BaseEntity;
import Local.shared.infrastructure.exception.BusinessRuleException;

import java.util.Objects;

@Getter
@ToString
@Entity(name = "comments")
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Embedded
    private CommentText commentText;

    @Embedded
    private CommentRating commentRating;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "is_hidden", nullable = false)
    private Boolean isHidden;

    protected Comment() {}

    public Comment(Customer customer, Long productId, String text, Integer rating) {
        validateCustomer(customer);
        validateProductId(productId);

        this.customer = customer;
        this.productId = productId;
        this.commentText = new CommentText(text);
        this.commentRating = new CommentRating(rating);
        this.isVerified = false;
        this.isHidden = false;
    }

    // Основные бизнес-методы
    public void updateComment(String newText, Integer newRating) {
        if (isHidden) {
            throw new BusinessRuleException("Cannot update hidden comment");
        }
        this.commentText = new CommentText(newText);
        this.commentRating = new CommentRating(newRating);
    }

    public void hide() {
        this.isHidden = true;
    }

    public void show() {
        this.isHidden = false;
    }

    public void verify() {
        this.isVerified = true;
    }

    // Методы запросов
    public boolean isVisible() {
        return !isHidden;
    }

    public boolean isPositive() {
        return commentRating.isPositive(); // >= 4
    }

    public boolean isNegative() {
        return commentRating.isNegative(); // <= 2
    }

    public String getCommentText() {
        return commentText.getContent();
    }

    public Integer getRating() {
        return commentRating.getValue();
    }

    //TODO: вынести в отдельный класс валидации
    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new BusinessRuleException("Product ID cannot be null");
        }
        if (productId <= 0) {
            throw new BusinessRuleException("Product ID must be positive");
        }
    }

    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new BusinessRuleException("Customer cannot be null");
        }
        if (!customer.getIsActive()) {
            throw new BusinessRuleException("Customer must be active to leave comments");
        }
    }

    // Equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return customer.equals(comment.customer) &&
                productId.equals(comment.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, productId);
    }
}