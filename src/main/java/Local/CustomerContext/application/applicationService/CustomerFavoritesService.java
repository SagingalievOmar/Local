package Local.CustomerContext.application.applicationService;

import Local.CustomerContext.application.mapper.CustomerFavoritesMapper;
import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.repository.CustomerRepository;
import Local.CustomerContext.application.DTO.request.AddToFavoritesRequest;
import Local.CustomerContext.application.DTO.request.RemoveFromFavoritesRequest;
import Local.CustomerContext.application.DTO.response.CustomerFavoritesDTO;
import Local.shared.domain.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Local.shared.infrastructure.exception.BusinessRuleException;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerFavoritesService {

    private final CustomerRepository customerRepository;
    private final CustomerFavoritesMapper favoritesMapper;
    private final DomainEventPublisher eventPublisher;

    public void addToFavorites(AddToFavoritesRequest request) {
        validateProductId(request.getProductId());

        Customer customer = findCustomerById(request.getCustomerId());
        customer.addToFavorites(request.getProductId());
        customerRepository.save(customer);
        eventPublisher.publishEventsFrom(customer);
    }

    public void removeFromFavorites(RemoveFromFavoritesRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());
        customer.removeFromFavorites(request.getProductId());
        customerRepository.save(customer);
        eventPublisher.publishEventsFrom(customer);
    }

    public void clearFavorites(Long customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer.getFavorites() != null) {
            customer.getFavorites().clear();
        }
        customerRepository.save(customer);
        eventPublisher.publishEventsFrom(customer);
    }

    @Transactional(readOnly = true)
    public CustomerFavoritesDTO getCustomerFavorites(Long customerId) {
        Customer customer = findCustomerById(customerId);
        return favoritesMapper.toFavoritesDTO(customer.getFavorites());
    }

    @Transactional(readOnly = true)
    public Integer getFavoritesCount(Long customerId) {
        Customer customer = findCustomerById(customerId);
        return customer.getFavorites() != null ? customer.getFavorites().getProductIds().size() : 0;
    }

    @Transactional(readOnly = true)
    public Boolean isProductInFavorites(Long customerId, Long productId) {
        Customer customer = findCustomerById(customerId);
        return customer.getFavorites() != null &&
                customer.getFavorites().getProductIds().contains(productId);
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessRuleException("Customer not found with id: " + customerId));
    }

    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required");
        }
    }
}
