package Local.CustomerContext.domain.service;

import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.model.valueObject.CustomerStatus;
import org.springframework.stereotype.Component;
import Local.shared.domain.valueObject.Money;

import java.math.BigDecimal;

@Component
public class CustomerStatusService {

    private static final Money VIP_THRESHOLD = Money.of(100000, "RUB");
    private static final int VIP_ORDERS_THRESHOLD = 10;
    private static final int REGULAR_ORDERS_THRESHOLD = 3;
    private static final int LOYALTY_ORDERS_THRESHOLD = 3;
    private static final Money POINTS_DIVISOR = Money.of(100, "RUB");

    public CustomerStatus calculateStatus(Customer customer, int totalOrders, Money totalSpent) {
        if (!customer.getIsActive()) {
            return CustomerStatus.INACTIVE;
        }

        if (!customer.isVerified()) {
            return CustomerStatus.UNVERIFIED;
        }

        if (customer.isFullyRegistered() &&
                totalOrders >= VIP_ORDERS_THRESHOLD &&
                totalSpent.isGreaterThan(VIP_THRESHOLD)) {
            return CustomerStatus.VIP;
        }

        if (customer.isFullyRegistered() && totalOrders >= REGULAR_ORDERS_THRESHOLD) {
            return CustomerStatus.REGULAR;
        }

        return CustomerStatus.NEW;
    }

    public boolean isEligibleForLoyaltyProgram(Customer customer, int totalOrders) {
        return customer.isVerified() &&
                customer.getIsActive() &&
                totalOrders >= LOYALTY_ORDERS_THRESHOLD;
    }

    public int calculateLoyaltyPoints(Customer customer, Money orderAmount) {
        if (!customer.getIsActive()) {
            return 0;
        }

        // Базовые очки: 1 очко за каждые 100 рублей
        BigDecimal basePoints = orderAmount.divide(100);

        // VIP клиенты получают в 2 раза больше
        if (customer.isFullyRegistered()) {
            return basePoints.multiply(BigDecimal.valueOf(2)).intValue();
        }

        return basePoints.intValue();
    }
}
