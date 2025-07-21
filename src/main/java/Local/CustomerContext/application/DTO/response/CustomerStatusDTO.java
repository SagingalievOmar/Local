package Local.CustomerContext.application.DTO.response;

import Local.CustomerContext.domain.model.valueObject.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerStatusDTO {
    private CustomerStatus status;
    private Boolean eligibleForLoyalty;
    private Integer loyaltyPoints;
}
