package Local.CustomerContext.application.DTO.request;

import lombok.Getter;

@Getter
public class RemoveFromCartRequest {
    private Long customerId;
    private Long productId;
}
