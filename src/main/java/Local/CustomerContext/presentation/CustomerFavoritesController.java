package Local.CustomerContext.presentation;

import Local.CustomerContext.application.DTO.request.AddToFavoritesRequest;
import Local.CustomerContext.application.DTO.request.RemoveFromFavoritesRequest;
import Local.CustomerContext.application.DTO.response.CustomerFavoritesDTO;
import Local.CustomerContext.application.applicationService.CustomerFavoritesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/favorites")
@RequiredArgsConstructor
@Tag(name = "Customer Favorites", description = "Customer favorites management endpoints")
public class CustomerFavoritesController {

    private final CustomerFavoritesService favoritesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add to favorites")
    public void addToFavorites(@PathVariable Long customerId,
                              @Valid @RequestBody AddToFavoritesRequest request) {
        favoritesService.addToFavorites(request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove from favorites")
    public void removeFromFavorites(@PathVariable Long customerId,
                                  @Valid @RequestBody RemoveFromFavoritesRequest request) {
        favoritesService.removeFromFavorites(request);
    }

    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Clear favorites")
    public void clearFavorites(@PathVariable Long customerId) {
        favoritesService.clearFavorites(customerId);
    }

    @GetMapping
    @Operation(summary = "Get customer favorites")
    public CustomerFavoritesDTO getFavorites(@PathVariable Long customerId) {
        return favoritesService.getCustomerFavorites(customerId);
    }

    @GetMapping("/count")
    @Operation(summary = "Get favorites count")
    public Integer getFavoritesCount(@PathVariable Long customerId) {
        return favoritesService.getFavoritesCount(customerId);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Check if product is in favorites")
    public Boolean isInFavorites(@PathVariable Long customerId,
                                @PathVariable Long productId) {
        return favoritesService.isProductInFavorites(customerId, productId);
    }
}
