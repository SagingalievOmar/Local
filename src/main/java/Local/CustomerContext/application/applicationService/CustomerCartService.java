package Local.CustomerContext.application.applicationService;

import Local.CustomerContext.application.DTO.request.AddToCartRequest;
import Local.CustomerContext.application.DTO.request.RemoveFromCartRequest;
import Local.CustomerContext.application.DTO.request.UpdateCartItemQuantityRequest;
import Local.CustomerContext.application.DTO.response.CustomerCartDTO;
import Local.CustomerContext.application.mapper.CustomerCartMapper;
import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.repository.CustomerRepository;
import Local.shared.domain.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Local.shared.infrastructure.exception.BusinessRuleException;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerCartService {

    private final CustomerRepository customerRepository;
    private final CustomerCartMapper cartMapper;
    private final DomainEventPublisher eventPublisher;

    public void addToCart(AddToCartRequest request) {
        validateAddToCartRequest(request);

        Customer customer = findCustomerById(request.getCustomerId());
        customer.addToCart(request.getProductId(), request.getQuantity());
        customerRepository.save(customer);

        eventPublisher.publishEventsFrom(customer);
    }

    public void removeFromCart(RemoveFromCartRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());
        customer.removeFromCart(request.getProductId());
        customerRepository.save(customer);

        eventPublisher.publishEventsFrom(customer);
    }

    public void updateCartItemQuantity(UpdateCartItemQuantityRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());

        customer.getCart().updateItemQuantity(request.getProductId(), request.getQuantity());
        customerRepository.save(customer);

        eventPublisher.publishEventsFrom(customer);
    }

    public void clearCart(Long customerId) {
        Customer customer = findCustomerById(customerId);
        customer.clearCart();
        customerRepository.save(customer);

        eventPublisher.publishEventsFrom(customer);
    }

    @Transactional(readOnly = true)
    public CustomerCartDTO getCustomerCart(Long customerId) {
        Customer customer = findCustomerById(customerId);

        return cartMapper.toCartDTO(customer.getCart());
    }

    @Transactional(readOnly = true)
    public Integer getCartItemsCount(Long customerId) {
        Customer customer = findCustomerById(customerId);
        return customer.getCart() != null ? customer.getCart().getItems().size() : 0;
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessRuleException("Customer not found with id: " + customerId));
    }

    private void validateAddToCartRequest(AddToCartRequest request) {
        if (request.getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

}
