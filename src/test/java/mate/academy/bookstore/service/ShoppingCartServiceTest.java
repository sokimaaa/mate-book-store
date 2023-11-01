package mate.academy.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.bookstore.dto.cart.request.AddToCartRequestDto;
import mate.academy.bookstore.dto.cart.request.UpdateCartItemRequestDto;
import mate.academy.bookstore.dto.cart.response.ShoppingCartDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.CartItemRepository;
import mate.academy.bookstore.repository.ShoppingCartRepository;
import mate.academy.bookstore.repository.UserRepository;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

  @InjectMocks
  private ShoppingCartServiceImpl shoppingCartService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private ShoppingCartRepository shoppingCartRepository;

  @Mock
  private CartItemRepository cartItemRepository;

  @Mock
  private ShoppingCartMapper shoppingCartMapper;

  @Test
  @DisplayName("Successfully retrieve shopping cart by user ID")
  public void getById_ShouldReturnShoppingCart() {
    Long userId = 1L;
    ShoppingCart shoppingCart = new ShoppingCart();

    when(shoppingCartRepository.findByUserIdWithCartItems(userId)).thenReturn(Optional.of(shoppingCart));
    when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(new ShoppingCartDto());

    ShoppingCartDto result = shoppingCartService.getById(userId);
    assertNotNull(result);
  }

  @Test
  @DisplayName("Successfully add item to cart")
  public void addToCart_ShouldReturnShoppingCart() {
    Long userId = 1L;
    AddToCartRequestDto request = new AddToCartRequestDto(1L, 1);

    User user = new User();
    Book book = new Book();
    ShoppingCartDto shoppingCartDto = new ShoppingCartDto();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(bookRepository.findById(request.bookId())).thenReturn(Optional.of(book));
    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());
    when(shoppingCartMapper.toDto(any(ShoppingCart.class)))
      .thenReturn(shoppingCartDto); // Mock the behavior of shoppingCartMapper

    ShoppingCartDto result = shoppingCartService.addToCart(request, userId);
    assertNotNull(result);
  }

  @Test
  @DisplayName("Successfully update cart item")
  public void updateCartItem_ShouldReturnUpdatedShoppingCart() {
    Long userId = 1L;
    Long cartItemId = 1L;
    UpdateCartItemRequestDto request = new UpdateCartItemRequestDto(2);

    ShoppingCart shoppingCart = new ShoppingCart();
    CartItem cartItem = new CartItem();
    ShoppingCartDto shoppingCartDto = new ShoppingCartDto();

    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
    when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId()))
      .thenReturn(Optional.of(cartItem));
    when(shoppingCartMapper.toDto(Mockito.any())).thenReturn(shoppingCartDto); // Updated mock behavior

    ShoppingCartDto result = shoppingCartService.updateCartItem(userId, cartItemId, request);
    assertNotNull(result);
  }

  @Test
  @DisplayName("Successfully remove cart item")
  public void removeCartItem_ShouldReturnUpdatedShoppingCart() {
    Long userId = 1L;
    Long cartItemId = 1L;

    ShoppingCart shoppingCart = new ShoppingCart();
    CartItem cartItem = new CartItem();
    ShoppingCartDto shoppingCartDto = new ShoppingCartDto();

    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
    when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId()))
      .thenReturn(Optional.of(cartItem));
    when(shoppingCartMapper.toDto(Mockito.any())).thenReturn(shoppingCartDto); // Updated mock behavior

    ShoppingCartDto result = shoppingCartService.removeCartItem(userId, cartItemId);
    assertNotNull(result);
  }

  @Test
  @DisplayName("Throw EntityNotFoundException when shopping cart is not found by userId")
  public void getById_ShouldThrowEntityNotFoundException() {
    Long userId = 1L;

    when(shoppingCartRepository.findByUserIdWithCartItems(userId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shoppingCartService.getById(userId));
  }

  @Test
  @DisplayName("Throw EntityNotFoundException when user is not found while adding to cart")
  public void addToCart_ShouldThrowEntityNotFoundExceptionForMissingUser() {
    Long userId = 1L;
    AddToCartRequestDto request = new AddToCartRequestDto(1L, 1);

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shoppingCartService.addToCart(request, userId));
  }

  @Test
  @DisplayName("Throw EntityNotFoundException when book is not found while adding to cart")
  public void addToCart_ShouldThrowEntityNotFoundExceptionForMissingBook() {
    Long userId = 1L;
    AddToCartRequestDto request = new AddToCartRequestDto(1L, 1);

    when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
    when(bookRepository.findById(request.bookId())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shoppingCartService.addToCart(request, userId));
  }

  @Test
  @DisplayName("Throw EntityNotFoundException when shopping cart is not found while updating cart item")
  public void updateCartItem_ShouldThrowEntityNotFoundExceptionForMissingCart() {
    Long userId = 1L;
    Long cartItemId = 1L;
    UpdateCartItemRequestDto request = new UpdateCartItemRequestDto(2);

    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shoppingCartService.updateCartItem(userId, cartItemId, request));
  }

  @Test
  @DisplayName("Throw EntityNotFoundException when cart item is not found while updating cart item")
  public void updateCartItem_ShouldThrowEntityNotFoundExceptionForMissingCartItem() {
    Long userId = 1L;
    Long cartItemId = 1L;
    UpdateCartItemRequestDto request = new UpdateCartItemRequestDto(2);

    ShoppingCart shoppingCart = new ShoppingCart();
    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
    when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shoppingCartService.updateCartItem(userId, cartItemId, request));
  }

  @Test
  @DisplayName("Throw EntityNotFoundException when shopping cart is not found while removing cart item")
  public void removeCartItem_ShouldThrowEntityNotFoundExceptionForMissingCart() {
    Long userId = 1L;
    Long cartItemId = 1L;

    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shoppingCartService.removeCartItem(userId, cartItemId));
  }

  @Test
  @DisplayName("Throw EntityNotFoundException when cart item is not found while removing cart item")
  public void removeCartItem_ShouldThrowEntityNotFoundExceptionForMissingCartItem() {
    Long userId = 1L;
    Long cartItemId = 1L;

    ShoppingCart shoppingCart = new ShoppingCart();
    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
    when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shoppingCartService.removeCartItem(userId, cartItemId));
  }
}
