package mate.academy.bookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.dto.order.OrderStatus;
import mate.academy.bookstore.dto.order.request.PlaceOrderRequestDto;
import mate.academy.bookstore.dto.order.request.UpdateOrderStatusRequestDto;
import mate.academy.bookstore.dto.order.response.OrderDto;
import mate.academy.bookstore.dto.order.response.OrderItemDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.OrderItemRepository;
import mate.academy.bookstore.repository.OrderRepository;
import mate.academy.bookstore.repository.ShoppingCartRepository;
import mate.academy.bookstore.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @Mock
  private OrderRepository orderRepository;
  @Mock
  private OrderItemRepository orderItemRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private OrderMapper orderMapper;
  @Mock
  private OrderItemMapper orderItemMapper;
  @Mock
  private ShoppingCartRepository shoppingCartRepository;

  @InjectMocks
  private OrderServiceImpl orderService;

  @Test
  void placeOrder_ShouldReturnOrderDto_WhenRequestIsValid() {
    // Given
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    ShoppingCart shoppingCart = new ShoppingCart();
    PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto("123 Main St");
    OrderDto orderDto = new OrderDto();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
    when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

    // When
    OrderDto result = orderService.placeOrder(userId, placeOrderRequestDto);

    // Then
    assertEquals(orderDto, result);
    verify(userRepository, times(1)).findById(userId);
    verify(shoppingCartRepository, times(1)).findByUserId(userId);
    verify(orderRepository, times(1)).save(any(Order.class));
    verify(orderMapper, times(1)).toDto(any(Order.class));
  }

  @Test
  void placeOrder_ShouldThrowException_WhenUserNotFound() {
    // Given
    Long userId = 1L;
    PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto("123 Main St");

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // When
    Exception exception = assertThrows(EntityNotFoundException.class, () ->
      orderService.placeOrder(userId, placeOrderRequestDto)
    );

    // Then
    assertEquals("User not found with ID: " + userId, exception.getMessage());
  }

  @Test
  void placeOrder_ShouldThrowException_WhenShoppingCartNotFound() {
    // Given
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto("123 Main St");

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());

    // When
    Exception exception = assertThrows(EntityNotFoundException.class, () ->
      orderService.placeOrder(userId, placeOrderRequestDto)
    );

    // Then
    assertEquals("Can't find shopping cart for user with id " + user, exception.getMessage());
  }

  @Test
  void placeOrder_ShouldCalculateTotalPriceCorrectly() {
    // Given
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    final PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto("123 Main St");

    final ShoppingCart shoppingCart = new ShoppingCart();
    Book book = new Book();
    book.setId(100L);
    book.setPrice(BigDecimal.valueOf(10));

    int quantity = 2;
    CartItem cartItem = new CartItem();
    cartItem.setQuantity(quantity);
    cartItem.setBook(book);
    shoppingCart.setCartItems(Collections.singletonList(cartItem));

    Order order = new Order();
    order.setUser(user);
    OrderItem orderItem = new OrderItem();
    orderItem.setQuantity(quantity);
    orderItem.setBook(book);
    orderItem.setPrice(BigDecimal.valueOf(10));
    order.setOrderItems(Collections.singleton(orderItem));

    OrderDto orderDto = new OrderDto();
    orderDto.setTotal(BigDecimal.valueOf(20));
    orderDto.setStatus(OrderStatus.PENDING);
    orderDto.setUserId(userId);

    OrderItemDto orderItemDto = new OrderItemDto();
    orderItemDto.setBookId(book.getId());
    orderItemDto.setQuantity(quantity);
    orderDto.setOrderItems(List.of(orderItemDto));
    when(orderRepository.save(any())).thenReturn(order);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
    when(orderMapper.toDto(order)).thenReturn(orderDto);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));

    // When
    OrderDto actualOrderDto = orderService.placeOrder(userId, placeOrderRequestDto);

    // Then
    BigDecimal expectedTotal = BigDecimal.valueOf(20);
    assertEquals(expectedTotal, actualOrderDto.getTotal());
  }

  @Test
  void placeOrder_ShouldCreateOrderWithItems_WhenShoppingCartHasItems() {
    // Given
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    final PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto("123 Main St");

    final ShoppingCart shoppingCart = new ShoppingCart();
    Book book = new Book();
    book.setId(100L);
    book.setPrice(BigDecimal.valueOf(10));

    int quantity = 2;
    CartItem cartItem = new CartItem();
    cartItem.setQuantity(quantity);
    cartItem.setBook(book);
    shoppingCart.setCartItems(Collections.singletonList(cartItem));

    Order order = new Order();
    order.setUser(user);
    OrderItem orderItem = new OrderItem();
    orderItem.setQuantity(quantity);
    orderItem.setBook(book);
    orderItem.setPrice(BigDecimal.valueOf(10));
    order.setOrderItems(Collections.singleton(orderItem));

    OrderDto orderDto = new OrderDto();
    orderDto.setTotal(BigDecimal.valueOf(20));
    orderDto.setStatus(OrderStatus.PENDING);
    orderDto.setUserId(userId);

    OrderItemDto orderItemDto = new OrderItemDto();
    orderItemDto.setBookId(book.getId());
    orderItemDto.setQuantity(quantity);
    orderDto.setOrderItems(List.of(orderItemDto));
    when(orderRepository.save(any())).thenReturn(order);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
    when(orderMapper.toDto(order)).thenReturn(orderDto);

    // When
    OrderDto actualOrderDto = orderService.placeOrder(userId, placeOrderRequestDto);

    // Then
    assertFalse(actualOrderDto.getOrderItems().isEmpty());
    assertEquals(1, actualOrderDto.getOrderItems().size());
    OrderItemDto firstOrderItem = actualOrderDto.getOrderItems().stream().findFirst().orElse(null);
    assertNotNull(firstOrderItem);
    assertEquals(cartItem.getQuantity(), firstOrderItem.getQuantity());
  }

  @Test
  void updateOrderStatus_ShouldUpdateAndReturnOrderDto_WhenValidRequest() {
    // Given
    Long orderId = 1L;
    OrderStatus newStatus = OrderStatus.DELIVERED;
    final UpdateOrderStatusRequestDto request = new UpdateOrderStatusRequestDto(orderId, newStatus);

    Order order = new Order();
    order.setId(orderId);
    order.setStatus(Order.Status.PENDING);

    OrderDto expectedOrderDto = new OrderDto();
    expectedOrderDto.setId(orderId);
    expectedOrderDto.setStatus(newStatus);

    when(orderRepository.save(any(Order.class))).thenReturn(order);
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
    when(orderMapper.toDto(any(Order.class))).thenReturn(expectedOrderDto);

    // When
    OrderDto actualOrderDto = orderService.updateOrderStatus(request);

    // Then
    assertEquals(expectedOrderDto, actualOrderDto);
    assertEquals(newStatus, actualOrderDto.getStatus());
    verify(orderRepository, times(1)).save(any(Order.class));
  }

  @Test
  void updateOrderStatus_ShouldThrowEntityNotFoundException_WhenOrderNotFound() {
    // Given
    Long orderId = 1L;
    UpdateOrderStatusRequestDto request = new UpdateOrderStatusRequestDto(orderId, OrderStatus.DELIVERED);

    when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(EntityNotFoundException.class, () -> orderService.updateOrderStatus(request));
  }

  @Test
  @DisplayName("Get order history successfully")
  public void getOrderHistory_ShouldReturnOrderList() {
    // Given
    Long userId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    Order order = new Order();

    when(orderRepository.findAllByUserId(userId, pageable)).thenReturn(Collections.singletonList(order));

    // When
    List<OrderDto> results = orderService.getOrderHistory(userId, pageable);

    // Then
    assertNotNull(results);
    assertEquals(1, results.size());
  }

  @Test
  @DisplayName("Get order items successfully")
  public void getOrderItems_ShouldReturnOrderItemList() {
    Long orderId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    OrderItem orderItem = new OrderItem();
    OrderItemDto orderItemDto = new OrderItemDto(); // Assuming you have a default constructor or use some real value.

    when(orderItemRepository.findAllByOrderId(orderId, pageable)).thenReturn(Collections.singletonList(orderItem));
    when(orderItemMapper.toDtoList(Collections.singletonList(orderItem)))
      .thenReturn(Collections.singletonList(orderItemDto));

    List<OrderItemDto> results = orderService.getOrderItems(orderId, pageable);
    assertNotNull(results);
    assertEquals(1, results.size());
  }

  @Test
  @DisplayName("Get order item successfully")
  public void getOrderItem_ShouldReturnOrderItem() {
    Long orderId = 1L;
    Long itemId = 1L;
    OrderItem orderItem = new OrderItem();
    OrderItemDto orderItemDto = new OrderItemDto(); // Assuming you have a default constructor or use some real value.

    when(orderItemRepository.findByIdAndOrderId(itemId, orderId)).thenReturn(Optional.of(orderItem));
    when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

    OrderItemDto result = orderService.getOrderItem(orderId, itemId);
    assertNotNull(result);
  }
}
