package Local.CustomerContext.application.handlers;

import Local.CustomerContext.domain.event.FavoritesEvent.FavoritesCreatedEvent;
import Local.CustomerContext.domain.event.FavoritesEvent.ProductAddedToFavoritesEvent;
import Local.CustomerContext.domain.event.FavoritesEvent.ProductRemovedFromFavoritesEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class FavoritesEventHandlers {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFavoritesCreated(FavoritesCreatedEvent event) {
        log.info("Favorites created for customer: {}", event.getCustomerFullName());

        // Трекинг создания списка избранного
        // Возможно, отправка информации о возможностях избранного
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductAddedToFavorites(ProductAddedToFavoritesEvent event) {
        log.info("Product {} added to favorites for user: {}", event.getProductId(), event.getCustomerId());

        // Трекинг добавления в избранное
        // Подписка на уведомления о скидках на этот товар
        // Анализ предпочтений пользователя
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductRemovedFromFavorites(ProductRemovedFromFavoritesEvent event) {
        log.info("Product {} removed from favorites for user: {}", event.getProductId(), event.getCustomerId());

        // Трекинг удаления из избранного
        // Отписка от уведомлений о скидках
        // Анализ изменения предпочтений
    }
}
