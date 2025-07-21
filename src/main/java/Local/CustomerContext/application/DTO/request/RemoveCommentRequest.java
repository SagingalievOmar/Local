package Local.CustomerContext.application.DTO.request;

import lombok.Getter;

@Getter
public class RemoveCommentRequest {
    private Long customerId;
    private Long productId;
}
