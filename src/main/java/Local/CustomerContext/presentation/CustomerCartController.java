package Local.CustomerContext.presentation;

import Local.CustomerContext.application.DTO.request.AddToCartRequest;
import Local.CustomerContext.application.DTO.request.RemoveFromCartRequest;
import Local.CustomerContext.application.DTO.request.UpdateCartItemQuantityRequest;
import Local.CustomerContext.application.DTO.response.CustomerCartDTO;
import Local.CustomerContext.application.applicationService.CustomerCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/cart")
@RequiredArgsConstructor
@Tag(name = "Customer Cart", description = "Customer cart management endpoints")
public class CustomerCartController {

    private final CustomerCartService cartService;

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add item to cart")
    public void addToCart(@PathVariable Long customerId,
                         @Valid @RequestBody AddToCartRequest request) {
        cartService.addToCart(request);
    }

    @DeleteMapping("/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove item from cart")
    public void removeFromCart(@PathVariable Long customerId,
                             @Valid @RequestBody RemoveFromCartRequest request) {
        cartService.removeFromCart(request);
    }

    @PutMapping("/items")
    @Operation(summary = "Update cart item quantity")
    public void updateCartItemQuantity(@PathVariable Long customerId,
                                     @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        cartService.updateCartItemQuantity(request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Clear cart")
    public void clearCart(@PathVariable Long customerId) {
        cartService.clearCart(customerId);
    }

    @GetMapping
    @Operation(summary = "Get customer cart")
    public CustomerCartDTO getCart(@PathVariable Long customerId) {
        return cartService.getCustomerCart(customerId);
    }

    @GetMapping("/count")
    @Operation(summary = "Get cart items count")
    public Integer getCartItemsCount(@PathVariable Long customerId) {
        return cartService.getCartItemsCount(customerId);
    }
}
