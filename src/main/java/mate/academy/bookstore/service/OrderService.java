package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.order.request.PlaceOrderRequestDto;
import mate.academy.bookstore.dto.order.request.UpdateOrderStatusRequestDto;
import mate.academy.bookstore.dto.order.response.OrderDto;
import mate.academy.bookstore.dto.order.response.OrderItemDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
  OrderDto placeOrder(Long userId, PlaceOrderRequestDto placeOrderRequestDto);

  List<OrderDto> getOrderHistory(Long userId, Pageable pageable);

  OrderDto updateOrderStatus(UpdateOrderStatusRequestDto request);

  List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable);

  OrderItemDto getOrderItem(Long orderId, Long itemId);
}
