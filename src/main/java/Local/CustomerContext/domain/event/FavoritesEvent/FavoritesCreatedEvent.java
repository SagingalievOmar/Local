package Local.CustomerContext.domain.event.FavoritesEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;

@Getter
@AllArgsConstructor
public class FavoritesCreatedEvent extends DomainEvent {
    private final String customerFullName;
}
