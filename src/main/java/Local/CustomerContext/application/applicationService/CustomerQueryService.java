package Local.CustomerContext.application.applicationService;

import Local.CustomerContext.application.mapper.CustomerStatusMapper;
import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.model.valueObject.CustomerStatus;
import Local.CustomerContext.domain.model.valueObject.Email;
import Local.CustomerContext.domain.repository.CustomerRepository;
import Local.CustomerContext.domain.service.CustomerStatusService;
import Local.CustomerContext.application.mapper.CustomerMapper;
import Local.CustomerContext.application.DTO.response.CustomerDTO;
import Local.CustomerContext.application.DTO.response.CustomerStatisticsDTO;
import Local.CustomerContext.application.DTO.response.CustomerStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Local.shared.domain.valueObject.Money;
import Local.shared.infrastructure.exception.BusinessRuleException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerQueryService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerStatusMapper customerStatusMapper;
    private final CustomerStatusService statusService;

    public CustomerDTO getCustomer(Long customerId) {
        Customer customer = findCustomerById(customerId);
        return customerMapper.toDTO(customer);
    }

    public Optional<CustomerDTO> getCustomerByEmail(String email) {
        Email emailVO = new Email(email);
        return customerRepository.findByEmail(emailVO)
                .map(customerMapper::toDTO);
    }

    public Optional<CustomerDTO> getCustomerByUserId(Long userId) {
        return customerRepository.findByUserId(userId)
                .map(customerMapper::toDTO);
    }

    public List<CustomerDTO> getActiveCustomers() {
        return customerRepository.findByIsActiveTrue()
                .stream()
                .map(customerMapper::toDTO)
                .toList();
    }

    public List<CustomerDTO> getCustomersByStatus(CustomerStatus status) {
        // Этот метод требует дополнительной логики с заказами
        // Пока возвращаем базовую фильтрацию
        return customerRepository.findByIsActiveTrue()
                .stream()
                .map(customerMapper::toDTO)
                .toList();
    }

    public CustomerStatusDTO getCustomerStatus(Long customerId, Integer totalOrders, String totalSpentAmount) {
        Customer customer = findCustomerById(customerId);

        int orders = totalOrders != null ? totalOrders : 0;
        Money totalSpent = totalSpentAmount != null ?
                Money.of(Double.parseDouble(totalSpentAmount), "RUB") :
                Money.of(0, "RUB");

        CustomerStatus status = statusService.calculateStatus(customer, orders, totalSpent);
        boolean eligibleForLoyalty = statusService.isEligibleForLoyaltyProgram(customer, orders);
        int loyaltyPoints = statusService.calculateLoyaltyPoints(customer, totalSpent);

        return new CustomerStatusDTO(status, eligibleForLoyalty, loyaltyPoints);
    }

    public CustomerStatisticsDTO getCustomerStatistics(Long customerId) {
        Customer customer = findCustomerById(customerId);
        return customerStatusMapper.toStatisticsDTO(customer);
    }


    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessRuleException("Customer not found with id: " + customerId));
    }
}
