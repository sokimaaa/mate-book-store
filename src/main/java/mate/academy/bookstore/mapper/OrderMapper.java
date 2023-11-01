package mate.academy.bookstore.mapper;

import java.util.List;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.order.response.OrderDto;
import mate.academy.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {

  @Mapping(source = "user.id", target = "userId")
  OrderDto toDto(Order order);

  @Mapping(source = "userId", target = "user.id")
  @Mapping(target = "orderItems", ignore = true)
  Order toEntity(OrderDto orderDto);

  List<OrderDto> toDtoList(List<Order> orders);

  List<Order> toEntityList(List<OrderDto> orderDtos);
}
