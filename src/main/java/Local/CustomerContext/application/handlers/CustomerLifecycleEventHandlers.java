package Local.CustomerContext.application.handlers;

import Local.CustomerContext.domain.event.CustomerEvent.CustomerAddressChangedEvent;
import Local.CustomerContext.domain.event.CustomerEvent.CustomerEmailChangedEvent;
import Local.CustomerContext.domain.event.CustomerEvent.CustomerPersonalInfoChangedEvent;
import Local.CustomerContext.domain.event.CustomerEvent.CustomerRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class CustomerLifecycleEventHandlers {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerRegistered(CustomerRegisteredEvent event) {
        log.info("Processing CustomerRegistered event for user: {}", event.getCustomerId());

        // Здесь можно добавить логику обработки регистрации
        // Например, отправка welcome email, создание профиля в других системах и т.д.

        log.info("Customer registered successfully: {} ({})", event.getCustomerName(), event.getEmail());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerPersonalInfoChanged(CustomerPersonalInfoChangedEvent event) {
        log.info("Personal info changed for user: {}", event.getCustomerId());
        log.info("Name changed from '{}' to '{}'", event.getOldName(), event.getNewName());
        log.info("Phone changed from '{}' to '{}'", event.getOldPhone(), event.getNewPhone());

        // Логика синхронизации профиля с другими контекстами
        // Отправка SMS для верификации нового телефона
        // Уведомление об изменениях профиля
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerAddressChanged(CustomerAddressChangedEvent event) {
        log.info("Address changed for user: {}", event.getCustomerId());
        log.info("Address changed from '{}' to '{}'", event.getOldAddress(), event.getNewAddress());

        // Синхронизация адреса с Order контекстом
        // Предложение товаров популярных в новом регионе
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerEmailChanged(CustomerEmailChangedEvent event) {
        log.info("Email changed for user: {} from {} to {}",
                event.getCustomerId(), event.getOldEmail(), event.getNewEmail());

        // Уведомление старого email об изменении
        // Отправка верификации на новый email
        // Синхронизация с Customer контекстом
    }
}
