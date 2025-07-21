package Local.CustomerContext.application.mapper;

import Local.CustomerContext.application.DTO.response.CartItemDTO;
import Local.CustomerContext.application.DTO.response.CustomerCartDTO;
import Local.CustomerContext.domain.model.Cart;
import Local.CustomerContext.domain.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerCartMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "items", target = "totalItems", qualifiedByName = "calculateTotalItems")
    @Mapping(source = "items", target = "totalQuantity", qualifiedByName = "calculateTotalQuantity")
    CustomerCartDTO toCartDTO(Cart cart);

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    CartItemDTO toCartItemDTO(CartItem cartItem);

    List<CartItemDTO> toCartItemDTOList(List<CartItem> cartItems);

    @Named("calculateTotalItems")
    default Integer calculateTotalItems(List<CartItem> items) {
        return items != null ? items.size() : 0;
    }

    @Named("calculateTotalQuantity")
    default Integer calculateTotalQuantity(List<CartItem> items) {
        return items != null ?
                items.stream().mapToInt(CartItem::getQuantityValue).sum() : 0;
    }

}
