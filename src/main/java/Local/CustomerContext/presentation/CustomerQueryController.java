package Local.CustomerContext.presentation;

import Local.CustomerContext.application.DTO.response.CustomerDTO;
import Local.CustomerContext.application.DTO.response.CustomerStatisticsDTO;
import Local.CustomerContext.application.DTO.response.CustomerStatusDTO;
import Local.CustomerContext.application.applicationService.CustomerQueryService;
import Local.CustomerContext.domain.model.valueObject.CustomerStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Queries", description = "Customer query endpoints")
public class CustomerQueryController {

    private final CustomerQueryService queryService;

    @GetMapping("/{customerId}")
    @Operation(summary = "Get customer by ID")
    public CustomerDTO getCustomer(@PathVariable Long customerId) {
        return queryService.getCustomer(customerId);
    }

    @GetMapping("/by-email")
    @Operation(summary = "Get customer by email")
    public CustomerDTO getCustomerByEmail(@RequestParam String email) {
        return queryService.getCustomerByEmail(email)
                .orElse(null);
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get customer by user ID")
    public CustomerDTO getCustomerByUserId(@PathVariable Long userId) {
        return queryService.getCustomerByUserId(userId)
                .orElse(null);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active customers")
    public List<CustomerDTO> getActiveCustomers() {
        return queryService.getActiveCustomers();
    }

    @GetMapping("/by-status/{status}")
    @Operation(summary = "Get customers by status")
    public List<CustomerDTO> getCustomersByStatus(@PathVariable CustomerStatus status) {
        return queryService.getCustomersByStatus(status);
    }

    @GetMapping("/{customerId}/status")
    @Operation(summary = "Get customer status")
    public CustomerStatusDTO getCustomerStatus(
            @PathVariable Long customerId,
            @RequestParam(required = false) Integer totalOrders,
            @RequestParam(required = false) String totalSpentAmount) {
        return queryService.getCustomerStatus(customerId, totalOrders, totalSpentAmount);
    }

    @GetMapping("/{customerId}/statistics")
    @Operation(summary = "Get customer statistics")
    public CustomerStatisticsDTO getCustomerStatistics(@PathVariable Long customerId) {
        return queryService.getCustomerStatistics(customerId);
    }
}
