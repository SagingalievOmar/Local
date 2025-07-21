package Local.CustomerContext.application.mapper;

import Local.CustomerContext.application.DTO.response.CustomerFavoritesDTO;
import Local.CustomerContext.domain.model.Favorites;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerFavoritesMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "productIds", target = "productIds")
    CustomerFavoritesDTO toFavoritesDTO(Favorites favorites);

}
