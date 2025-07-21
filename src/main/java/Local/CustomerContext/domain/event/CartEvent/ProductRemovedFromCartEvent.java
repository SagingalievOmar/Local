package Local.CustomerContext.domain.event.CartEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;

@Getter
@AllArgsConstructor
public class ProductRemovedFromCartEvent extends DomainEvent {
    private final Long customerId;
    private final Long productId;
}
