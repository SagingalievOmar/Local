package Local.CustomerContext.domain.service;

import Local.CustomerContext.domain.model.valueObject.Email;
import Local.CustomerContext.domain.model.valueObject.PhoneNumber;
import Local.CustomerContext.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerUniquenessService {

    private final CustomerRepository customerRepository;

    public boolean isEmailAvailable(Email email) {
        return !customerRepository.existsByEmail(email);
    }

    public boolean isEmailAvailableForUpdate(Email email, Long excludeCustomerId) {
        return !customerRepository.existsByEmailAndIdNot(email, excludeCustomerId);
    }

    public boolean isPhoneAvailable(PhoneNumber phone) {
        return !customerRepository.existsByPhoneNumber(phone);
    }

    public boolean isPhoneAvailableForUpdate(PhoneNumber phone, Long excludeCustomerId) {
        return !customerRepository.existsByPhoneNumberAndIdNot(phone, excludeCustomerId);
    }

    public boolean isUserIdAvailable(Long userId) {
        return !customerRepository.existsByUserId(userId);
    }
}