package Local.CustomerContext.application.DTO.request;

import Local.CustomerContext.application.DTO.response.AddressData;
import lombok.Getter;
import jakarta.validation.constraints.*;

@Getter
public class CreateCustomerRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "Address cannot be null")
    private AddressData address;
}
