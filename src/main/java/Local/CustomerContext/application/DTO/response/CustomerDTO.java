package Local.CustomerContext.application.DTO.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressData address;
    private Boolean isActive;
    private Boolean emailVerified;
    private Boolean phoneVerified;
}
