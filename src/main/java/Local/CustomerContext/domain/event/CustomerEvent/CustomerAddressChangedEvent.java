package Local.CustomerContext.domain.event.CustomerEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;

@Getter
@AllArgsConstructor
public class CustomerAddressChangedEvent extends DomainEvent {
    private final Long customerId;
    private final String oldAddress;
    private final String newAddress;
}
