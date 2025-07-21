package Local.CustomerContext.application.DTO.request;

import lombok.Getter;
import jakarta.validation.constraints.*;

@Getter
public class UpdateCommentRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotBlank(message = "Comment text cannot be blank")
    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String text;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;
}
