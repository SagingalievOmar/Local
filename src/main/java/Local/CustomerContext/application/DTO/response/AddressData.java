package Local.CustomerContext.application.DTO.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressData {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
