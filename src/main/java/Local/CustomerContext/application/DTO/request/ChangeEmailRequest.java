package Local.CustomerContext.application.DTO.request;

import lombok.Getter;
import jakarta.validation.constraints.*;

@Getter
public class ChangeEmailRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String newEmail;
}
