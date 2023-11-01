package mate.academy.bookstore.mapper;

import java.util.List;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.order.response.OrderItemDto;
import mate.academy.bookstore.model.OrderItem;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
  @Mapping(source = "book.id", target = "bookId")
  @Named(value = "toDto")
  OrderItemDto toDto(OrderItem orderItem);

  @IterableMapping(qualifiedByName = "toDto")
  List<OrderItemDto> toDtoList(List<OrderItem> orderItems);
}
