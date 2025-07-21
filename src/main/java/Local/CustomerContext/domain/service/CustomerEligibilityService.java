package Local.CustomerContext.domain.service;

import Local.CustomerContext.domain.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerEligibilityService {

    public boolean canAddComment(Customer customer, Long productId) {
        return !customer.getIsActive() &&
                !customer.isVerified() &&
                !customer.hasCommentForProduct(productId);
    }

    public boolean canMakeOrder(Customer customer) {
        return customer.getIsActive() &&
                customer.getEmailVerified() &&
                customer.hasActiveCart();
    }

    public boolean requiresAddressForOrder(Customer customer) {
        // Бизнес-правило: для заказа нужен адрес
        return customer.getAddress() == null;
    }
}
