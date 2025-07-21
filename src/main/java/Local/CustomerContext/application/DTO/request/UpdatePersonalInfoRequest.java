package Local.CustomerContext.application.DTO.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UpdatePersonalInfoRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Invalid phone number format")
    private String phone;
}
