package Local.CustomerContext.application.DTO.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerFavoritesDTO {
    private Long customerId;
    private List<Long> productIds;
    private Integer totalProducts;
}
