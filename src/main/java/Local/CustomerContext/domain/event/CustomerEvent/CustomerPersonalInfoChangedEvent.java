package Local.CustomerContext.domain.event.CustomerEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;

@Getter
@AllArgsConstructor
public class CustomerPersonalInfoChangedEvent extends DomainEvent {
    private final Long customerId;
    private final String oldName;
    private final String newName;
    private final String oldPhone;
    private final String newPhone;
}
