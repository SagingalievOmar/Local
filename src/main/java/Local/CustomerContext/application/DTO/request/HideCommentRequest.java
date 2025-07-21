package Local.CustomerContext.application.DTO.request;

import lombok.Getter;

@Getter
public class HideCommentRequest {
    private Long customerId;
    private Long productId;
}
