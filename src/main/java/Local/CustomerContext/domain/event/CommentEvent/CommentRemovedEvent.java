package Local.CustomerContext.domain.event.CommentEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;


@Getter
@AllArgsConstructor
public class CommentRemovedEvent extends DomainEvent {
    private final Long customerId;
    private final Long productId;
}
