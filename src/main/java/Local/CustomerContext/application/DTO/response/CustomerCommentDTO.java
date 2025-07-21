package Local.CustomerContext.application.DTO.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerCommentDTO {
    private Long id;
    private Long customerId;
    private Long productId;
    private String commentText;
    private Integer rating;
    private Boolean isVisible;
    private String createdAt;
    private String updatedAt;
}
