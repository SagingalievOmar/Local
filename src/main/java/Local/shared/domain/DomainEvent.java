package Local.shared.domain;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
public abstract class DomainEvent {

    private final String eventId;
    private final LocalDateTime occurredOn;
    private final String eventType;

    public DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
        this.eventType = this.getClass().getSimpleName();
    }

}
