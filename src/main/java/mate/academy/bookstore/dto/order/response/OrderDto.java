package mate.academy.bookstore.dto.order.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import mate.academy.bookstore.dto.order.OrderStatus;

@Data
public class OrderDto {
  private Long id;
  private Long userId;
  private List<OrderItemDto> orderItems;
  private LocalDateTime orderDate;
  private BigDecimal total;
  private OrderStatus status;
}
