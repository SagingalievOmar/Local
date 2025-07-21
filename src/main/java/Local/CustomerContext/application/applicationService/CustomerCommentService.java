package Local.CustomerContext.application.applicationService;

import Local.CustomerContext.application.DTO.request.AddCommentRequest;
import Local.CustomerContext.application.DTO.request.HideCommentRequest;
import Local.CustomerContext.application.DTO.request.RemoveCommentRequest;
import Local.CustomerContext.application.DTO.request.UpdateCommentRequest;
import Local.CustomerContext.application.DTO.response.CustomerCommentDTO;
import Local.CustomerContext.application.mapper.CustomerCommentMapper;
import Local.CustomerContext.domain.model.Comment;
import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.repository.CustomerRepository;
import Local.CustomerContext.domain.service.CustomerEligibilityService;
import Local.shared.domain.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Local.shared.infrastructure.exception.BusinessRuleException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerCommentService {

    private final CustomerRepository customerRepository;
    private final CustomerCommentMapper commentMapper;
    private final CustomerEligibilityService eligibilityService;
    private final DomainEventPublisher eventPublisher;

    public void addComment(AddCommentRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());

        if (!eligibilityService.canAddComment(customer, request.getProductId())) {
            throw new BusinessRuleException("Customer is not eligible to add comment");
        }

        customer.addComment(request.getProductId(), request.getText(), request.getRating());
        customerRepository.save(customer);

        eventPublisher.publishEventsFrom(customer);
    }

    public void updateComment(UpdateCommentRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());
        customer.updateComment(request.getProductId(), request.getText(), request.getRating());
        customerRepository.save(customer);
        eventPublisher.publishEventsFrom(customer);
    }

    public void hideComment(HideCommentRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());
        customer.hideComment(request.getProductId());
        customerRepository.save(customer);
        eventPublisher.publishEventsFrom(customer);
    }

    public void removeComment(RemoveCommentRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());
        customer.removeComment(request.getProductId());
        customerRepository.save(customer);
        eventPublisher.publishEventsFrom(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerCommentDTO> getCustomerComments(Long customerId) {
        Customer customer = findCustomerById(customerId);
        return commentMapper.toCommentDTOList(customer.getActiveComments());
    }

    @Transactional(readOnly = true)
    public CustomerCommentDTO getCustomerCommentForProduct(Long customerId, Long productId) {
        Customer customer = findCustomerById(customerId);
        return customer.getComments().stream()
                .filter(comment -> comment.getProductId().equals(productId))
                .filter(Comment::isVisible)
                .findFirst()
                .map(commentMapper::toCommentDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Boolean hasCommentForProduct(Long customerId, Long productId) {
        Customer customer = findCustomerById(customerId);
        return customer.hasCommentForProduct(productId);
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessRuleException("Customer not found with id: " + customerId));
    }

}
