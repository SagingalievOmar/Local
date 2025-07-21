package Local.CustomerContext.domain.event.CartEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;

@Getter
@AllArgsConstructor
public class CartCreatedEvent extends DomainEvent {
    private final String customerFullName;
}
