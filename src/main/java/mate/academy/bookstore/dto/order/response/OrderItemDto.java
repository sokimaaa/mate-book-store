package mate.academy.bookstore.dto.order.response;

import lombok.Data;

@Data
public class OrderItemDto {
  private Long id;
  private Long bookId;
  private int quantity;
}
