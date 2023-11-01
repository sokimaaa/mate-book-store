package mate.academy.bookstore.service.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
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
import mate.academy.bookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final UserRepository userRepository;
  private final OrderMapper orderMapper;
  private final OrderItemMapper orderItemMapper;
  private final ShoppingCartRepository shoppingCartRepository;

  @Override
  @Transactional
  public OrderDto placeOrder(Long userId, PlaceOrderRequestDto placeOrderRequestDto) {
    User user = userRepository.findById(userId)
      .orElseThrow(EntityNotFoundException.supplier("User not found with ID: " + userId));

    ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
      .orElseThrow(EntityNotFoundException.supplier("Can't find shopping cart for user with id " + user));

    BigDecimal total = BigDecimal.ZERO;
    for (CartItem cartItem : shoppingCart.getCartItems()) {
      BigDecimal itemPrice = cartItem.getBook().getPrice()
        .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
      total = total.add(itemPrice);
    }
    Order order = new Order();
    order.setUser(user);
    order.setStatus(Order.Status.valueOf(OrderStatus.PENDING.name()));
    order.setShippingAddress(placeOrderRequestDto.shippingAddress());
    order.setTotal(total);
    order = orderRepository.save(order);

    Set<OrderItem> orderItems = new HashSet<>(shoppingCart.getCartItems().size());
    for (CartItem cartItem : shoppingCart.getCartItems()) {
      Book book = cartItem.getBook();

      OrderItem orderItem = new OrderItem();
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setBook(book);
      orderItem.setOrder(order);
      orderItem.setPrice(book.getPrice());
      orderItemRepository.save(orderItem);
      orderItems.add(orderItem);
    }
    order.setOrderItems(orderItems);
    return orderMapper.toDto(order);
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderDto> getOrderHistory(Long userId, Pageable pageable) {
    List<Order> orders = orderRepository.findAllByUserId(userId, pageable);
    return orders.stream()
      .map(orderMapper::toDto)
      .toList();
  }

  @Override
  @Transactional
  public OrderDto updateOrderStatus(UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
    Order order = orderRepository.findById(updateOrderStatusRequestDto.orderId())
      .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: "
        + updateOrderStatusRequestDto.orderId()));

    order.setStatus(Order.Status.valueOf(updateOrderStatusRequestDto.status().name()));
    order = orderRepository.save(order);
    return orderMapper.toDto(order);
  }

  public List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable) {
    List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId, pageable);
    return orderItemMapper.toDtoList(orderItems);
  }

  public OrderItemDto getOrderItem(Long orderId, Long itemId) {
    OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
      .orElseThrow(() -> new EntityNotFoundException("OrderItem with id " + itemId + " not found in order with id "
        + orderId));
    return orderItemMapper.toDto(orderItem);
  }
}
