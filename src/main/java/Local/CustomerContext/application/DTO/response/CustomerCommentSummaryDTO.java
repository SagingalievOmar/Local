package Local.CustomerContext.application.DTO.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerCommentSummaryDTO {
    private Long customerId;
    private Long productId;
    private Integer rating;
    private Boolean isVisible;
}
