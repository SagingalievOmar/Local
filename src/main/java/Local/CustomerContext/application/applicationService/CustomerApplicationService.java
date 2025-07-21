package Local.CustomerContext.application.applicationService;

import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.model.valueObject.Address;
import Local.CustomerContext.domain.model.valueObject.CustomerName;
import Local.CustomerContext.domain.model.valueObject.Email;
import Local.CustomerContext.domain.model.valueObject.PhoneNumber;
import Local.CustomerContext.domain.repository.CustomerRepository;
import Local.CustomerContext.domain.service.CustomerValidationService;
import Local.CustomerContext.application.mapper.CustomerMapper;
import Local.CustomerContext.application.DTO.request.ChangeEmailRequest;
import Local.CustomerContext.application.DTO.request.CreateCustomerRequest;
import Local.CustomerContext.application.DTO.request.UpdateAddressRequest;
import Local.CustomerContext.application.DTO.request.UpdatePersonalInfoRequest;
import Local.CustomerContext.application.DTO.response.CustomerDTO;
import Local.shared.domain.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Local.shared.infrastructure.exception.BusinessRuleException;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerApplicationService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerValidationService validationService;
    private final DomainEventPublisher eventPublisher;

    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        CustomerName name = new CustomerName(request.getFirstName(), request.getLastName());
        Email email = new Email(request.getEmail());
        PhoneNumber phone = new PhoneNumber(request.getPhone());
        Address address = request.getAddress() != null ?
                customerMapper.toAddress(request.getAddress()) : null;

        validationService.validateForRegistration(request.getUserId(), email, phone);

        Customer customer = address != null ?
                new Customer(request.getUserId(), name, email, phone, address) :
                new Customer(request.getUserId(), name, email, phone);

        Customer savedCustomer = customerRepository.save(customer);
        eventPublisher.publishEventsFrom(savedCustomer);

        return customerMapper.toDTO(savedCustomer);
    }

    public CustomerDTO updatePersonalInfo(UpdatePersonalInfoRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());

        CustomerName newName = new CustomerName(request.getFirstName(), request.getLastName());
        PhoneNumber newPhone = new PhoneNumber(request.getPhone());

        validationService.validateForPhoneChange(customer.getId(), newPhone);

        customer.updatePersonalInfo(newName, newPhone);
        Customer savedCustomer = customerRepository.save(customer);
        eventPublisher.publishEventsFrom(savedCustomer);

        return customerMapper.toDTO(savedCustomer);
    }

    public CustomerDTO updateAddress(UpdateAddressRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());

        Address newAddress = customerMapper.toAddress(request.getAddress());
        customer.updateAddress(newAddress);

        Customer savedCustomer = customerRepository.save(customer);
        eventPublisher.publishEventsFrom(savedCustomer);

        return customerMapper.toDTO(savedCustomer);
    }

    public CustomerDTO changeEmail(ChangeEmailRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());

        Email newEmail = new Email(request.getNewEmail());
        validationService.validateForEmailChange(customer.getId(), newEmail);

        customer.changeEmail(newEmail);
        Customer savedCustomer = customerRepository.save(customer);
        eventPublisher.publishEventsFrom(savedCustomer);

        return customerMapper.toDTO(savedCustomer);
    }

    public void verifyEmail(Long customerId) {
        Customer customer = findCustomerById(customerId);
        customer.verifyEmail();
        customerRepository.save(customer);
    }

    public void verifyPhone(Long customerId) {
        Customer customer = findCustomerById(customerId);
        customer.verifyPhone();
        customerRepository.save(customer);
    }

    public void activateCustomer(Long customerId) {
        Customer customer = findCustomerById(customerId);
        customer.activate();
        customerRepository.save(customer);
    }

    public void deactivateCustomer(Long customerId) {
        Customer customer = findCustomerById(customerId);
        validationService.validateCanDeactivate(customer);
        customer.deactivate();
        customerRepository.save(customer);
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessRuleException("Customer not found with id: " + customerId));
    }
}
