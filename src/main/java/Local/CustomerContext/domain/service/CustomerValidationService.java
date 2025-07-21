package Local.CustomerContext.domain.service;

import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.model.valueObject.Email;
import Local.CustomerContext.domain.model.valueObject.PhoneNumber;
import Local.CustomerContext.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import Local.shared.infrastructure.exception.BusinessRuleException;

@Component
@RequiredArgsConstructor
public class CustomerValidationService {

    private final CustomerRepository customerRepository;

    public void validateForRegistration(Long userId, Email email, PhoneNumber phone) {
        if (customerRepository.existsByUserId(userId)) {
            throw new BusinessRuleException("Customer already exists for this user");
        }

        if (customerRepository.existsByEmail(email)) {
            throw new BusinessRuleException("Email already in use");
        }

        if (customerRepository.existsByPhoneNumber(phone)) {
            throw new BusinessRuleException("Phone number already in use");
        }
    }

    public void validateForEmailChange(Long customerId, Email newEmail) {
        if (customerRepository.existsByEmailAndIdNot(newEmail, customerId)) {
            throw new BusinessRuleException("Email already in use by another customer");
        }
    }

    public void validateForPhoneChange(Long customerId, PhoneNumber newPhone) {
        if (customerRepository.existsByPhoneNumberAndIdNot(newPhone, customerId)) {
            throw new BusinessRuleException("Phone number already in use by another customer");
        }
    }

    public void validateCanDeactivate(Customer customer) {
        if (customer.hasActiveCart()) {
            throw new BusinessRuleException("Cannot deactivate customer with active cart. Clear cart first.");
        }
    }
}