package Local.CustomerContext.application.DTO.request;

import Local.CustomerContext.application.DTO.response.AddressData;
import lombok.Getter;

@Getter
public class UpdateAddressRequest {
    private Long customerId;
    private AddressData address;
}
