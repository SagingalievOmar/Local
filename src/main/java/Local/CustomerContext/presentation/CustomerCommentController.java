package Local.CustomerContext.presentation;

import Local.CustomerContext.application.DTO.request.AddCommentRequest;
import Local.CustomerContext.application.DTO.request.HideCommentRequest;
import Local.CustomerContext.application.DTO.request.RemoveCommentRequest;
import Local.CustomerContext.application.DTO.request.UpdateCommentRequest;
import Local.CustomerContext.application.DTO.response.CustomerCommentDTO;
import Local.CustomerContext.application.applicationService.CustomerCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/comments")
@RequiredArgsConstructor
@Tag(name = "Customer Comments", description = "Customer comments management endpoints")
public class CustomerCommentController {

    private final CustomerCommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add comment")
    public void addComment(@PathVariable Long customerId,
                         @Valid @RequestBody AddCommentRequest request) {
        commentService.addComment(request);
    }

    @PutMapping
    @Operation(summary = "Update comment")
    public void updateComment(@PathVariable Long customerId,
                            @Valid @RequestBody UpdateCommentRequest request) {
        commentService.updateComment(request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove comment")
    public void removeComment(@PathVariable Long customerId,
                            @Valid @RequestBody RemoveCommentRequest request) {
        commentService.removeComment(request);
    }

    @PutMapping("/hide")
    @Operation(summary = "Hide comment")
    public void hideComment(@PathVariable Long customerId,
                          @Valid @RequestBody HideCommentRequest request) {
        commentService.hideComment(request);
    }

    @GetMapping
    @Operation(summary = "Get customer comments")
    public List<CustomerCommentDTO> getComments(@PathVariable Long customerId) {
        return commentService.getCustomerComments(customerId);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get customer comment for product")
    public CustomerCommentDTO getCommentForProduct(@PathVariable Long customerId,
                                                 @PathVariable Long productId) {
        return commentService.getCustomerCommentForProduct(customerId, productId);
    }

    @GetMapping("/{productId}/exists")
    @Operation(summary = "Check if comment exists for product")
    public Boolean hasCommentForProduct(@PathVariable Long customerId,
                                      @PathVariable Long productId) {
        return commentService.hasCommentForProduct(customerId, productId);
    }
}
