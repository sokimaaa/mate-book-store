package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cart.request.AddToCartRequestDto;
import mate.academy.bookstore.dto.cart.request.UpdateCartItemRequestDto;
import mate.academy.bookstore.dto.cart.response.ShoppingCartDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
  private final ShoppingCartService shoppingCartService;

  @GetMapping
  @Operation(summary = "Get a shopping cart")
  public ShoppingCartDto getShoppingCart(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return shoppingCartService.getById(user.getId());
  }

  @PostMapping
  @Operation(summary = "Add an item to a cart")
  public ShoppingCartDto addToCart(
    @RequestBody AddToCartRequestDto request,
    Authentication authentication
  ) {
    User user = (User) authentication.getPrincipal();
    return shoppingCartService.addToCart(request, user.getId());
  }

  @PutMapping("/cart-items/{id}")
  @Operation(summary = "Update an item quantity in a cart")
  public ShoppingCartDto updateCartItem(
    @PathVariable Long id,
    @RequestBody UpdateCartItemRequestDto request,
    Authentication authentication
  ) {
    User user = (User) authentication.getPrincipal();
    return shoppingCartService.updateCartItem(user.getId(), id, request);
  }

  @DeleteMapping("/cart-items/{id}")
  @Operation(summary = "Remove an item from a cart")
  public ShoppingCartDto removeCartItem(
    @PathVariable Long id,
    Authentication authentication
  ) {
    User user = (User) authentication.getPrincipal();
    return shoppingCartService.removeCartItem(user.getId(), id);
  }
}
