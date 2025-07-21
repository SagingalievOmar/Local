package Local.CustomerContext.domain.model;

import Local.CustomerContext.domain.model.valueObject.Quantity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import Local.shared.domain.BaseEntity;
import Local.shared.infrastructure.exception.BusinessRuleException;

import java.math.BigDecimal;

@Getter
@ToString(exclude = "cart")
@Entity(name = "cart_items")
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    protected CartItem() {
    }

    public CartItem(Cart cart, Long productId, Quantity quantity) {
        validateCart(cart);
        validateProductId(productId);
        validateQuantity(quantity);

        this.cart = cart;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = BigDecimal.ZERO; //TODO: Будет установлена позже из каталога
        this.totalPrice = BigDecimal.ZERO;
    }

    // Основные бизнес-методы
    public void updateQuantity(Quantity newQuantity) {
        validateQuantity(newQuantity);

        this.quantity = newQuantity;
        recalculateTotal();
    }

    public void increaseQuantity(Integer additionalQuantity) {
        validateAdditionalQuantity(additionalQuantity);

        int newQuantityValue = this.quantity.getValue() + additionalQuantity;
        this.quantity = new Quantity(newQuantityValue);
        recalculateTotal();
    }

    public void decreaseQuantity(Integer decreaseAmount) {
        validateDecreaseAmount(decreaseAmount);

        int newQuantityValue = this.quantity.getValue() - decreaseAmount;
        if (newQuantityValue <= 0) {
            throw new BusinessRuleException("Quantity cannot be less than or equal to zero");
        }

        this.quantity = new Quantity(newQuantityValue);
        recalculateTotal();
    }

    public void updatePrice(BigDecimal newUnitPrice) {
        validatePrice(newUnitPrice);

        this.unitPrice = newUnitPrice;
        recalculateTotal();
    }

    // Методы запросов
    public boolean isSameProduct(Long productId) {
        return this.productId.equals(productId);
    }

    public boolean hasValidPrice() {
        return unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getSubtotal() {
        return totalPrice != null ? totalPrice : BigDecimal.ZERO;
    }

    public int getQuantityValue() {
        return quantity.getValue();
    }

    // Приватные методы
    private void recalculateTotal() {
        if (unitPrice != null) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity.getValue()));
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    // Валидации
    private void validateCart(Cart cart) {
        if (cart == null) {
            throw new BusinessRuleException("Cart cannot be null");
        }
    }

    //TODO: нужно вынести в отдельный класс валидации
    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new BusinessRuleException("Product ID cannot be null");
        }
        if (productId <= 0) {
            throw new BusinessRuleException("Product ID must be positive");
        }
    }

    private void validateQuantity(Quantity quantity) {
        if (quantity == null) {
            throw new BusinessRuleException("Quantity cannot be null");
        }
        if (quantity.getValue() <= 0) {
            throw new BusinessRuleException("Quantity must be positive");
        }
    }

    private void validateAdditionalQuantity(Integer additionalQuantity) {
        if (additionalQuantity == null) {
            throw new BusinessRuleException("Additional quantity cannot be null");
        }
        if (additionalQuantity <= 0) {
            throw new BusinessRuleException("Additional quantity must be positive");
        }

        // Проверка на максимальное количество
        int newTotal = this.quantity.getValue() + additionalQuantity;
        if (newTotal > 999) { // Максимальное количество товара в корзине
            throw new BusinessRuleException("Maximum quantity exceeded");
        }
    }

    private void validateDecreaseAmount(Integer decreaseAmount) {
        if (decreaseAmount == null) {
            throw new BusinessRuleException("Decrease amount cannot be null");
        }
        if (decreaseAmount <= 0) {
            throw new BusinessRuleException("Decrease amount must be positive");
        }
        if (decreaseAmount >= this.quantity.getValue()) {
            throw new BusinessRuleException("Decrease amount cannot be greater than or equal to current quantity");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new BusinessRuleException("Price cannot be null");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Price cannot be negative");
        }
    }

    // Методы для интеграции с другими контекстами
    public void applyDiscount(BigDecimal discountPercent) {
        if (discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) < 0 ||
                discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BusinessRuleException("Invalid discount percentage");
        }

        if (hasValidPrice()) {
            BigDecimal discountAmount = unitPrice.multiply(discountPercent).divide(BigDecimal.valueOf(100));
            BigDecimal discountedPrice = unitPrice.subtract(discountAmount);
            updatePrice(discountedPrice);
        }
    }

    public void resetToOriginalPrice(BigDecimal originalPrice) {
        validatePrice(originalPrice);
        updatePrice(originalPrice);
    }

    // Equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return cart.equals(cartItem.cart) && productId.equals(cartItem.productId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(cart, productId);
    }

    // Дополнительные методы для удобства
    public String getDisplayInfo() {
        return String.format("Product: %d, Quantity: %d, Price: %s, Total: %s",
                productId, quantity.getValue(),
                unitPrice != null ? unitPrice.toString() : "N/A",
                totalPrice != null ? totalPrice.toString() : "N/A");
    }
}
