package Local.CustomerContext.domain.model;


import Local.CustomerContext.domain.event.CartEvent.CartClearedEvent;
import Local.CustomerContext.domain.event.CartEvent.CartCreatedEvent;
import Local.CustomerContext.domain.event.CartEvent.ProductAddedToCartEvent;
import Local.CustomerContext.domain.event.CartEvent.ProductRemovedFromCartEvent;
import Local.CustomerContext.domain.event.CommentEvent.CommentAddedEvent;
import Local.CustomerContext.domain.event.CommentEvent.CommentHiddenEvent;
import Local.CustomerContext.domain.event.CommentEvent.CommentRemovedEvent;
import Local.CustomerContext.domain.event.CommentEvent.CommentUpdatedEvent;
import Local.CustomerContext.domain.event.CustomerEvent.CustomerAddressChangedEvent;
import Local.CustomerContext.domain.event.CustomerEvent.CustomerEmailChangedEvent;
import Local.CustomerContext.domain.event.CustomerEvent.CustomerPersonalInfoChangedEvent;
import Local.CustomerContext.domain.event.CustomerEvent.CustomerRegisteredEvent;
import Local.CustomerContext.domain.event.FavoritesEvent.FavoritesCreatedEvent;
import Local.CustomerContext.domain.event.FavoritesEvent.ProductAddedToFavoritesEvent;
import Local.CustomerContext.domain.event.FavoritesEvent.ProductRemovedFromFavoritesEvent;
import Local.CustomerContext.domain.model.valueObject.Address;
import Local.CustomerContext.domain.model.valueObject.CustomerName;
import Local.CustomerContext.domain.model.valueObject.Email;
import Local.CustomerContext.domain.model.valueObject.PhoneNumber;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import Local.shared.domain.AggregateRoot;
import Local.shared.infrastructure.exception.BusinessRuleException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@Entity(name = "customers")
public class Customer extends AggregateRoot {

    @Column(nullable = false, unique = true)
    private Long userId;

    @Embedded
    private CustomerName customerName;

    @Embedded
    private Email email;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Address address;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified;

    @Column(name = "phone_verified", nullable = false)
    private Boolean phoneVerified;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Favorites favorites;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Comment> comments = new ArrayList<>();

    protected Customer() {
    }

    public Customer(Long userId, CustomerName customerName, Email email, PhoneNumber phoneNumber, Address address) {
        validateUserId(userId);
        validateCustomerName(customerName);
        validateEmail(email);
        validatePhoneNumber(phoneNumber);
        validateAddress(address);

        this.userId = userId;
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isActive = true;
        this.emailVerified = false;
        this.phoneVerified = false;

        this.cart = new Cart(this);
        this.favorites = new Favorites(this);

        addDomainEvent(new CustomerRegisteredEvent(userId, email.getEmail(), customerName.getFullName()));
        addDomainEvent(new CartCreatedEvent(customerName.getFullName()));
        addDomainEvent(new FavoritesCreatedEvent(customerName.getFullName()));
    }

    public Customer(Long userId, CustomerName customerName, Email email, PhoneNumber phoneNumber) {
        this(userId, customerName, email, phoneNumber, null);
    }

    // Бизнес-методы

    public void updatePersonalInfo(CustomerName newName, PhoneNumber newPhoneNumber) {
        validateCustomerName(newName);
        validatePhoneNumber(newPhoneNumber);

        this.customerName = newName;
        this.phoneNumber = newPhoneNumber;
        this.phoneVerified = false;

        addDomainEvent(new CustomerPersonalInfoChangedEvent(
                this.userId,
                this.customerName.getFullName(),
                newName.getFullName(),
                this.phoneNumber.getValue(),
                newPhoneNumber.getValue()));
    }

    public void updateAddress(Address newAddress) {
        validateAddress(newAddress);
        this.address = newAddress;

        addDomainEvent(new CustomerAddressChangedEvent(
                this.userId,
                address.getFullAddress(),
                newAddress.getFullAddress()));
    }

    public void changeEmail(Email newEmail) {
        validateEmail(newEmail);
        this.email = newEmail;
        this.emailVerified = false;

        addDomainEvent(new CustomerEmailChangedEvent(
                this.userId,
                email.getEmail(),
                newEmail.getEmail()));
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void verifyPhone() {
        this.phoneVerified = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    // Методы для работы с корзиной
    public void addToCart(Long productId, Integer quantity) {
        ensureActive();
        if (cart == null) {
            this.cart = new Cart(this);
        }
        cart.addItem(productId, quantity);

        addDomainEvent(new ProductAddedToCartEvent(this.userId, productId, quantity));
    }

    public void removeFromCart(Long productId) {
        ensureActive();
        if (cart != null) {
            cart.removeItem(productId);
        }

        addDomainEvent(new ProductRemovedFromCartEvent(this.userId, productId));
    }

    public void clearCart() {
        ensureActive();
        if (cart != null) {
            cart.clear();
        }
        addDomainEvent(new CartClearedEvent(this.userId));
    }

    // Методы для работы с избранным
    public void addToFavorites(Long productId) {
        ensureActive();
        if (favorites == null) {
            this.favorites = new Favorites(this);
        }
        favorites.addProduct(productId);

        addDomainEvent(new ProductAddedToFavoritesEvent(this.userId, productId));
    }

    public void removeFromFavorites(Long productId) {
        ensureActive();
        if (favorites != null) {
            favorites.removeProduct(productId);
        }

        addDomainEvent(new ProductRemovedFromFavoritesEvent(this.userId, productId));
    }

    // Методы для работы с комментариями
    public void addComment(Long productId, String text, Integer rating) {
        ensureActive();

        // Проверяем, нет ли уже комментария на этот продукт
        if (hasCommentForProduct(productId)) {
            throw new BusinessRuleException("Customer already has comment for this product");
        }

        Comment comment = new Comment(this, productId, text, rating);
        comments.add(comment);

        addDomainEvent(new CommentAddedEvent(this.userId, productId, rating, text));
    }

    public void updateComment(Long productId, String newText, Integer newRating) {
        ensureActive();

        Comment comment = findCommentForProduct(productId);
        if (comment == null) {
            throw new BusinessRuleException("No comment found for this product");
        }

        comment.updateComment(newText, newRating);

        addDomainEvent(new CommentUpdatedEvent(this.userId, productId, newRating, newText));
    }

    public void hideComment(Long productId) {
        ensureActive();

        Comment comment = findCommentForProduct(productId);
        if (comment == null) {
            throw new BusinessRuleException("No comment found for this product");
        }

        comment.hide();

        addDomainEvent(new CommentHiddenEvent(this.userId, productId));
    }

    public void removeComment(Long productId) {
        ensureActive();

        Comment comment = findCommentForProduct(productId);
        if (comment != null) {
            comments.remove(comment);
        }

        addDomainEvent(new CommentRemovedEvent(this.userId, productId));
    }

    // Вспомогательные методы
    public boolean hasCommentForProduct(Long productId) {
        return comments.stream()
                .anyMatch(comment -> comment.getProductId().equals(productId) && comment.isVisible());
    }

    public boolean hasComments() {
        return !comments.isEmpty();
    }

    public List<Comment> getActiveComments() {
        return comments.stream()
                .filter(Comment::isVisible)
                .collect(Collectors.toList());
    }

    private Comment findCommentForProduct(Long productId) {
        return comments.stream()
                .filter(comment -> comment.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    // Проверки состояния
    public boolean isVerified() {
        return emailVerified && phoneVerified;
    }

    public boolean isFullyRegistered() {
        return address != null && isVerified();
    }

    public boolean hasActiveCart() {
        return cart != null && cart.hasItems();
    }

    public boolean hasFavorites() {
        return favorites != null && favorites.hasProducts();
    }

    // Приватные методы валидации
    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new BusinessRuleException("User ID cannot be null");
        }
    }

    private void validateCustomerName(CustomerName customerName) {
        if (customerName == null) {
            throw new BusinessRuleException("Customer name cannot be null");
        }
    }

    private void validateEmail(Email email) {
        if (email == null) {
            throw new BusinessRuleException("Email cannot be null");
        }
    }

    private void validatePhoneNumber(PhoneNumber phoneNumber) {
        if (phoneNumber == null) {
            throw new BusinessRuleException("Phone number cannot be null");
        }
    }

    private void validateAddress(Address address) {
        // Address может быть null при создании, но если передан, то должен быть валидным
        // Валидация внутри самого Address value object
    }

    private void ensureActive() {
        if (!isActive) {
            throw new BusinessRuleException("Customer is not active");
        }
    }

    // Для проверки на equals и hashCode - используем email как уникальный идентификатор
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return email.equals(customer.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

}