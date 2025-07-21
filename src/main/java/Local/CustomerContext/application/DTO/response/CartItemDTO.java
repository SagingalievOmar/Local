package Local.CustomerContext.application.DTO.response;

import Local.CustomerContext.domain.model.valueObject.Quantity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long productId;
    private Quantity quantity;
}
