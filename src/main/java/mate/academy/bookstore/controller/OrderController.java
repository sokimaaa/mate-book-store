package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.request.PlaceOrderRequestDto;
import mate.academy.bookstore.dto.order.request.UpdateOrderStatusRequestDto;
import mate.academy.bookstore.dto.order.response.OrderDto;
import mate.academy.bookstore.dto.order.response.OrderItemDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Operations pertaining to orders in Order Management")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  @Operation(summary = "Place a new order")
  public OrderDto placeOrder(
    @RequestBody PlaceOrderRequestDto placeOrderRequestDto,
    Authentication authentication
  ) {
    User user = (User) authentication.getPrincipal();
    // find by email also could be an option here
    return orderService.placeOrder(user.getId(), placeOrderRequestDto);
  }

  @GetMapping
  @Operation(summary = "Get orders history")
  public List<OrderDto> getOrderHistory(
    Authentication authentication,
    Pageable pageable
  ) {
    User user = (User) authentication.getPrincipal();
    return orderService.getOrderHistory(user.getId(), pageable);
  }

  @PatchMapping
  @Operation(summary = "Update an order status")
  public OrderDto updateOrderStatus(@RequestBody UpdateOrderStatusRequestDto request) {
    return orderService.updateOrderStatus(request);
  }

  @GetMapping("/{orderId}/items")
  @Operation(summary = "Get order items")
  public List<OrderItemDto> getAll(
    @PathVariable Long orderId,
    Pageable pageable
  ) {
    return orderService.getOrderItems(orderId, pageable);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{orderId}/items/{itemId}")
  @Operation(summary = "Get order item details")
  public OrderItemDto getOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
    return orderService.getOrderItem(orderId, itemId);
  }
}
