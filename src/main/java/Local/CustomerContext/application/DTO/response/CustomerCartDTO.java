package Local.CustomerContext.application.DTO.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerCartDTO {
    private Long customerId;
    private List<CartItemDTO> items;
    private Integer totalItems;
    private Integer totalQuantity;
}
