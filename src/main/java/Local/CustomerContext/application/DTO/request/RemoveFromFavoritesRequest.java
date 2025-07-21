package Local.CustomerContext.application.DTO.request;

import lombok.Getter;
import jakarta.validation.constraints.*;

@Getter
public class RemoveFromFavoritesRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;
}
