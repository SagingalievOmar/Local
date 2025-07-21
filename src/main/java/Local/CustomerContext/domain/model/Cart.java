package Local.CustomerContext.domain.model;

import Local.CustomerContext.domain.model.valueObject.Quantity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import Local.shared.domain.BaseEntity;
import Local.shared.infrastructure.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity(name = "carts")
@ToString(exclude = {"customer", "items"})
public class Cart extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "items_count", nullable = false)
    private Integer itemsCount;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    protected Cart() {
    }

    public Cart(Customer customer) {
        validateCustomer(customer);
        this.customer = customer;
        this.items = new ArrayList<>();
        this.itemsCount = 0;
        this.isActive = true;
    }

    // Основные бизнес-методы

    public void addItem(Long productId, Integer quantity) {
        validateProductId(productId);
        validateQuantity(quantity);
        ensureActive();

        Optional<CartItem> existingItem = findItemByProductId(productId);

        if (existingItem.isPresent()) {
            existingItem.get().increaseQuantity(quantity);
        } else {
            CartItem newItem = new CartItem(this, productId, new Quantity(quantity));
            items.add(newItem);
        }

        recalculateCart();
    }

    public void removeItem(Long productId) {
        validateProductId(productId);
        ensureActive();

        Optional<CartItem> itemToRemove = findItemByProductId(productId);
        if (itemToRemove.isPresent()) {
            items.remove(itemToRemove.get());
            recalculateCart();
        }
    }

    public void updateItemQuantity(Long productId, Integer newQuantity) {
        validateProductId(productId);
        validateQuantity(newQuantity);
        ensureActive();

        Optional<CartItem> item = findItemByProductId(productId);
        if (item.isPresent()) {
            item.get().updateQuantity(new Quantity(newQuantity));
            recalculateCart();
        } else {
            throw new BusinessRuleException("Product not found in cart: " + productId);
        }
    }

    public void clear() {
        ensureActive();
        items.clear();
        recalculateCart();
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    // Методы запросов

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public boolean containsProduct(Long productId) {
        return findItemByProductId(productId).isPresent();
    }

    public Optional<CartItem> getItem(Long productId) {
        return findItemByProductId(productId);
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    public int getTotalItemsCount() {
        return items.stream()
                .mapToInt(item -> item.getQuantity().getValue())
                .sum();
    }

    public int getUniqueItemsCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    // Приватные методы

    private Optional<CartItem> findItemByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    private void recalculateCart() {
        this.itemsCount = getTotalItemsCount();

        // Здесь в реальном приложении нужно будет получать цены продуктов
        // Пока что устанавливаем в 0, так как у нас нет доступа к каталогу
        BigDecimal totalAmount = BigDecimal.ZERO;
        // TODO: Интеграция с каталогом для получения цен
        // totalAmount = calculateTotalWithPrices();

    }

    // Валидации

    //TODO: нужно вынести в отдельный класс валидации
    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new BusinessRuleException("Customer cannot be null");
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

    private void validateQuantity(Integer quantity) {
        if (quantity == null) {
            throw new BusinessRuleException("Quantity cannot be null");
        }
        if (quantity <= 0) {
            throw new BusinessRuleException("Quantity must be positive");
        }
    }

    private void ensureActive() {
        if (!isActive) {
            throw new BusinessRuleException("Cart is not active");
        }
    }

    // Методы для интеграции с другими контекстами

    public void applyPricing(List<ProductPrice> prices) {
        // Метод для применения цен из каталога
        // TODO: Реализовать когда будет готов каталог
    }

    // Equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return customer.equals(cart.customer);
    }

    @Override
    public int hashCode() {
        return customer.hashCode();
    }

    // Inner class для цен продуктов (временное решение)
    public record ProductPrice(Long productId, BigDecimal price) {}
}

