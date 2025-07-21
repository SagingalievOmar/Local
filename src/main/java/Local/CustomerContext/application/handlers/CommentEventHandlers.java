package Local.CustomerContext.application.handlers;

import Local.CustomerContext.domain.event.CommentEvent.CommentAddedEvent;
import Local.CustomerContext.domain.event.CommentEvent.CommentHiddenEvent;
import Local.CustomerContext.domain.event.CommentEvent.CommentRemovedEvent;
import Local.CustomerContext.domain.event.CommentEvent.CommentUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class CommentEventHandlers {


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentAdded(CommentAddedEvent event) {
        log.info("Comment added by user {} for product {}: rating {} - '{}'",
                event.getCustomerId(), event.getProductId(), event.getRating(), event.getCommentText());

        // Уведомление системы рейтингов о новом отзыве
        // Если высокий рейтинг, предложение поделиться в соцсетях  
        // Отправка на модерацию (если требуется)
        // Уведомление продавца о новом отзыве
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentUpdated(CommentUpdatedEvent event) {
        log.info("Comment updated by user {} for product {}: new rating {} - '{}'",
                event.getCustomerId(), event.getProductId(), event.getNewRating(), event.getNewCommentText());

        // Обновление рейтинга товара
        // Трекинг изменения отзыва
        // Повторная модерация (если требуется)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentHidden(CommentHiddenEvent event) {
        log.info("Comment hidden by user {} for product {}", event.getCustomerId(), event.getProductId());

        // Обновление рейтинга товара (исключение скрытого комментария)
        // Трекинг скрытия комментария
        // Уведомление модераторов (если нужно)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentRemoved(CommentRemovedEvent event) {
        log.info("Comment removed by user {} for product {}", event.getCustomerId(), event.getProductId());

        // Пересчет рейтинга товара
        // Трекинг удаления комментария
        // Очистка связанных данных
    }
}
