package Local.CustomerContext.domain.event.CommentEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Local.shared.domain.DomainEvent;

@Getter
@AllArgsConstructor
public class CommentUpdatedEvent extends DomainEvent {
    private final Long customerId;
    private final Long productId;
    private final Integer newRating;
    private final String newCommentText;
}
