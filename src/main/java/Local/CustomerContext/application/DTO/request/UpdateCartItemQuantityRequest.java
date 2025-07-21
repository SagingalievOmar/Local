package Local.CustomerContext.application.DTO.request;

import lombok.Getter;
import jakarta.validation.constraints.*;

@Getter
public class UpdateCartItemQuantityRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity must not exceed 100")
    private Integer quantity;
}
