package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cart.request.AddToCartRequestDto;
import mate.academy.bookstore.dto.cart.request.UpdateCartItemRequestDto;
import mate.academy.bookstore.dto.cart.response.ShoppingCartDto;

public interface ShoppingCartService {
  ShoppingCartDto getById(Long id);

  ShoppingCartDto addToCart(AddToCartRequestDto request, Long id);

  ShoppingCartDto updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequestDto request);

  ShoppingCartDto removeCartItem(Long userId, Long cartItemId);
}
