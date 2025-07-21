package Local.CustomerContext.presentation;

import Local.CustomerContext.application.DTO.request.ChangeEmailRequest;
import Local.CustomerContext.application.DTO.request.CreateCustomerRequest;
import Local.CustomerContext.application.DTO.request.UpdateAddressRequest;
import Local.CustomerContext.application.DTO.request.UpdatePersonalInfoRequest;
import Local.CustomerContext.application.DTO.response.CustomerDTO;
import Local.CustomerContext.application.applicationService.CustomerApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerApplicationService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new customer")
    public CustomerDTO createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @PutMapping("/{customerId}/personal-info")
    @Operation(summary = "Update customer personal information")
    public CustomerDTO updatePersonalInfo(@PathVariable Long customerId,
                                        @Valid @RequestBody UpdatePersonalInfoRequest request) {
        return customerService.updatePersonalInfo(request);
    }

    @PutMapping("/{customerId}/address")
    @Operation(summary = "Update customer address")
    public CustomerDTO updateAddress(@PathVariable Long customerId,
                                   @Valid @RequestBody UpdateAddressRequest request) {
        return customerService.updateAddress(request);
    }

    @PutMapping("/{customerId}/email")
    @Operation(summary = "Change customer email")
    public CustomerDTO changeEmail(@PathVariable Long customerId,
                                 @Valid @RequestBody ChangeEmailRequest request) {
        return customerService.changeEmail(request);
    }

    @PostMapping("/{customerId}/verify-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Verify customer email")
    public void verifyEmail(@PathVariable Long customerId) {
        customerService.verifyEmail(customerId);
    }

    @PostMapping("/{customerId}/verify-phone")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Verify customer phone")
    public void verifyPhone(@PathVariable Long customerId) {
        customerService.verifyPhone(customerId);
    }

    @PostMapping("/{customerId}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Activate customer")
    public void activateCustomer(@PathVariable Long customerId) {
        customerService.activateCustomer(customerId);
    }

    @PostMapping("/{customerId}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deactivate customer")
    public void deactivateCustomer(@PathVariable Long customerId) {
        customerService.deactivateCustomer(customerId);
    }
}
