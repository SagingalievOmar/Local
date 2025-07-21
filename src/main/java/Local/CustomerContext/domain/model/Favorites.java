package Local.CustomerContext.domain.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import Local.shared.domain.BaseEntity;
import Local.shared.infrastructure.exception.BusinessRuleException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString(exclude = "customer")
@Entity(name = "favorites")
public class Favorites extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ElementCollection
    @CollectionTable(name = "favorite_products", joinColumns = @JoinColumn(name = "favorites_id"))
    @Column(name = "product_id")
    private Set<Long> productIds = new HashSet<>();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    protected Favorites() {
    }

    public Favorites(Customer customer) {
        validateCustomer(customer);
        this.customer = customer;
        this.productIds = new HashSet<>();
        this.isActive = true;
    }

    // Основные бизнес-методы
    public void addProduct(Long productId) {
        validateProductId(productId);
        ensureActive();
        ensureNotFull();

        productIds.add(productId);
    }

    public void removeProduct(Long productId) {
        validateProductId(productId);
        ensureActive();

        productIds.remove(productId);
    }

    public void toggleProduct(Long productId) {
        validateProductId(productId);
        ensureActive();

        if (containsProduct(productId)) {
            removeProduct(productId);
        } else {
            addProduct(productId);
        }
    }

    public void clear() {
        ensureActive();
        if (!productIds.isEmpty()) {
            productIds.clear();
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    // Методы запросов
    public boolean hasProducts() {
        return !productIds.isEmpty();
    }

    public boolean containsProduct(Long productId) {
        return productIds.contains(productId);
    }

    public Set<Long> getProductIds() {
        return new HashSet<>(productIds);
    }

    public List<Long> getProductIdsAsList() {
        return new ArrayList<>(productIds);
    }

    public int getProductCount() {
        return productIds.size();
    }

    public boolean isEmpty() {
        return productIds.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean isFull() {
        return productIds.size() >= getMaxItemsLimit();
    }

    public int getAvailableSlots() {
        return getMaxItemsLimit() - productIds.size();
    }

    // Методы для интеграции с корзиной
    public void moveToCart(Long productId) {
        validateProductId(productId);
        ensureActive();

        if (!containsProduct(productId)) {
            throw new BusinessRuleException("Product not found in favorites: " + productId);
        }

        customer.addToCart(productId, 1);
        // Оставляем товар в избранном после добавления в корзину
    }

    public void moveAllToCart() {
        ensureActive();

        for (Long productId : new HashSet<>(productIds)) {
            try {
                customer.addToCart(productId, 1);
            } catch (BusinessRuleException e) {
                // Логируем ошибку но продолжаем с другими товарами
                // TODO: Добавить логирование
            }
        }
    }

    public void moveToCartAndRemove(Long productId) {
        validateProductId(productId);
        ensureActive();

        if (!containsProduct(productId)) {
            throw new BusinessRuleException("Product not found in favorites: " + productId);
        }

        customer.addToCart(productId, 1);
        removeProduct(productId);
    }

    // Batch операции
    public void addMultipleProducts(Set<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }

        ensureActive();

        for (Long productId : productIds) {
            validateProductId(productId);
            if (isFull()) {
                throw new BusinessRuleException("Cannot add more products. Favorites is full");
            }
            this.productIds.add(productId);
        }
    }

    public void removeMultipleProducts(Set<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }

        ensureActive();

        boolean hasChanges = false;
        for (Long productId : productIds) {
            validateProductId(productId);
            if (this.productIds.remove(productId)) {
                hasChanges = true;
            }
        }
    }

    // Приватные методы
    private int getMaxItemsLimit() {
        return 100; // Максимальное количество товаров в избранном
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

    private void ensureActive() {
        if (!isActive) {
            throw new BusinessRuleException("Favorites is not active");
        }
    }

    private void ensureNotFull() {
        if (isFull()) {
            throw new BusinessRuleException("Favorites list is full. Maximum " + getMaxItemsLimit() + " items allowed");
        }
    }

    // Методы для аналитики и статистики
    public boolean hasCommonProducts(Favorites other) {
        if (other == null) {
            return false;
        }

        return productIds.stream()
                .anyMatch(other.productIds::contains);
    }

    public Set<Long> getCommonProducts(Favorites other) {
        if (other == null) {
            return new HashSet<>();
        }

        Set<Long> common = new HashSet<>(productIds);
        common.retainAll(other.productIds);
        return common;
    }

    public int getCommonProductsCount(Favorites other) {
        return getCommonProducts(other).size();
    }

    // Equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorites favorites = (Favorites) o;
        return customer.equals(favorites.customer);
    }

    @Override
    public int hashCode() {
        return customer.hashCode();
    }

}
