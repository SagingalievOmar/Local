package Local.CustomerContext.domain.repository;

import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.model.valueObject.Email;
import Local.CustomerContext.domain.model.valueObject.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(Email email);

    boolean existsByEmail(Email email);

    boolean existsByEmailAndIdNot(Email email, Long id);

    boolean existsByPhoneNumber(PhoneNumber phoneNumber);

    boolean existsByPhoneNumberAndIdNot(PhoneNumber phoneNumber, Long id);

    boolean existsByUserId(Long userId);

    Optional<Customer> findByUserId(Long userId);

    List<Customer> findByIsActiveTrue();
}