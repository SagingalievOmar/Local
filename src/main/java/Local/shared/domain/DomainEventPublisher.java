package Local.shared.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishEventsFrom(AggregateRoot aggregate) {
        List<DomainEvent> events = aggregate.getDomainEvents();
        aggregate.clearDomainEvents();

        events.forEach(eventPublisher::publishEvent);
    }
}
