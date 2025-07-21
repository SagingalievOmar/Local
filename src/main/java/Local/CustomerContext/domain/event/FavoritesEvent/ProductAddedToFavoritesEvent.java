package Local.CustomerContext.domain.event.FavoritesEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;

@Getter
@AllArgsConstructor
public class ProductAddedToFavoritesEvent extends DomainEvent {
    private final Long customerId;
    private final Long productId;
}
