package Local.shared.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class AggregateRoot extends BaseEntity {

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    @Version
    private Long version;

    public void addDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public boolean hasDomainEvents() {
        return !domainEvents.isEmpty();
    }

    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

}
