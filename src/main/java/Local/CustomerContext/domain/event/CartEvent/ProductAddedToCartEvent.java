package Local.CustomerContext.domain.event.CartEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;

@Getter
@AllArgsConstructor
public class ProductAddedToCartEvent extends DomainEvent {
    private final Long customerId;
    private final Long productId;
    private final Integer quantity;
}
