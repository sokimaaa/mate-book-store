package mate.academy.bookstore.dto.order.request;

import mate.academy.bookstore.dto.order.OrderStatus;

public record UpdateOrderStatusRequestDto(Long orderId, OrderStatus status) {
}
