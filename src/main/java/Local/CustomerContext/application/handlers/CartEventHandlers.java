package Local.CustomerContext.application.handlers;

import Local.CustomerContext.domain.event.CartEvent.CartClearedEvent;
import Local.CustomerContext.domain.event.CartEvent.CartCreatedEvent;
import Local.CustomerContext.domain.event.CartEvent.ProductAddedToCartEvent;
import Local.CustomerContext.domain.event.CartEvent.ProductRemovedFromCartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class CartEventHandlers {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCartCreated(CartCreatedEvent event) {
        log.info("Cart created for customer: {}", event.getCustomerFullName());

        // Трекинг создания корзины
        // Возможно отправка информации о том, как пользоваться корзиной
        // Промо-код на первую покупку
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductAddedToCart(ProductAddedToCartEvent event) {
        log.info("Product {} added to cart for user: {} (quantity: {})",
                event.getProductId(), event.getCustomerId(), event.getQuantity());

        // Трекинг для рекомендательной системы
        // Предложение сопутствующих товаров
        // Обновление популярности товара
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductRemovedFromCart(ProductRemovedFromCartEvent event) {
        log.info("Product {} removed from cart for user: {}", event.getProductId(), event.getCustomerId());

        // Трекинг удаления товара из корзины
        // Анализ причин отказа от покупки
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCartCleared(CartClearedEvent event) {
        log.info("Cart cleared for user: {}", event.getCustomerId());

        // Трекинг очистки корзины
        // Email с предложением вернуться к покупкам
        // Анализ брошенных корзин
    }
}
