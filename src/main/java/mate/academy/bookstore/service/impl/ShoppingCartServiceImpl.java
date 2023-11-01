package mate.academy.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
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
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
  private final UserRepository userRepository;

  private final BookRepository bookRepository;

  private final ShoppingCartRepository shoppingCartRepository;

  private final CartItemRepository cartItemRepository;

  private final ShoppingCartMapper shoppingCartMapper;

  @Transactional(readOnly = true)
  public ShoppingCartDto getById(Long userId) {
    ShoppingCart shoppingCart = shoppingCartRepository.findByUserIdWithCartItems(userId)
      .orElseThrow(EntityNotFoundException.supplier("Can't find shopping cart by user with id " + userId));
    return shoppingCartMapper.toDto(shoppingCart);
  }

  @Transactional
  public ShoppingCartDto addToCart(AddToCartRequestDto request, Long userId) {
    User user = userRepository.findById(userId)
      .orElseThrow(EntityNotFoundException.supplier("User not found"));
    Book book = bookRepository.findById(request.bookId())
      .orElseThrow(EntityNotFoundException.supplier("Book not found"));

    ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseGet(() -> {
      ShoppingCart newShoppingCart = new ShoppingCart();
      newShoppingCart.setUser(user);
      shoppingCartRepository.save(newShoppingCart);
      return newShoppingCart;
    });

    CartItem cartItem = new CartItem();
    cartItem.setBook(book);
    cartItem.setShoppingCart(shoppingCart);
    cartItem.setQuantity(request.quantity());
    cartItemRepository.save(cartItem);

    return shoppingCartMapper.toDto(shoppingCart);
  }

  @Transactional
  public ShoppingCartDto updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequestDto request) {
    ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
      .orElseThrow(EntityNotFoundException.supplier("Can't find shopping cart by id " + userId));
    CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
      .orElseThrow(EntityNotFoundException.supplier(
        "Can't find cart item by id %s for user with id %s".formatted(cartItemId, userId))
      );
    cartItem.setQuantity(request.quantity());
    cartItemRepository.save(cartItem);
    return shoppingCartMapper.toDto(cartItem.getShoppingCart());
  }

  @Transactional
  public ShoppingCartDto removeCartItem(Long userId, Long cartItemId) {
    ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
      .orElseThrow(EntityNotFoundException.supplier("Can't find shopping cart by id " + userId));
    CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
      .orElseThrow(EntityNotFoundException.supplier(
        "Can't find cart item by id %s for user with id %s".formatted(cartItemId, userId))
      );
    cartItemRepository.delete(cartItem);
    return shoppingCartMapper.toDto(cartItem.getShoppingCart());
  }
}
