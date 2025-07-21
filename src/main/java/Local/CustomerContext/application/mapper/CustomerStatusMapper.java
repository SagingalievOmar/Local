package Local.CustomerContext.application.mapper;

import Local.CustomerContext.application.DTO.response.CustomerStatisticsDTO;
import Local.CustomerContext.application.DTO.response.CustomerStatusDTO;
import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.model.valueObject.CustomerStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerStatusMapper {

    default CustomerStatisticsDTO toStatisticsDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        int cartItemsCount = customer.getCart() != null ? customer.getCart().getItems().size() : 0;
        int favoritesCount = customer.getFavorites() != null ? customer.getFavorites().getProductIds().size() : 0;
        int commentsCount = customer.getActiveComments().size();

        return new CustomerStatisticsDTO(
                customer.getId(),
                cartItemsCount,
                favoritesCount,
                commentsCount,
                customer.isVerified(),
                customer.isFullyRegistered()
        );
    }

    default CustomerStatusDTO toStatusDTO(CustomerStatus status, Boolean eligibleForLoyalty, Integer loyaltyPoints) {
        return new CustomerStatusDTO(status, eligibleForLoyalty, loyaltyPoints);
    }
}
