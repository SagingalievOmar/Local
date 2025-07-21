package Local.CustomerContext.application.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerStatisticsDTO {
    private Long customerId;
    private Integer cartItemsCount;
    private Integer favoritesCount;
    private Integer commentsCount;
    private Boolean isVerified;
    private Boolean isFullyRegistered;
}
