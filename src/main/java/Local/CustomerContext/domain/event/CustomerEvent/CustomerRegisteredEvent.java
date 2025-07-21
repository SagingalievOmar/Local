package Local.CustomerContext.domain.event.CustomerEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;

@Getter
@AllArgsConstructor
public class CustomerRegisteredEvent extends DomainEvent {
    private final Long customerId;
    private final String email;
    private final String customerName;
}
