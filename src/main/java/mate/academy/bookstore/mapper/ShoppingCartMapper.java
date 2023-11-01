package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.cart.response.ShoppingCartDto;
import mate.academy.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "cartItems", target = "cartItems")
  ShoppingCartDto toDto(ShoppingCart shoppingCart);

  @Mapping(source = "userId", target = "user.id")
  @Mapping(source = "cartItems", target = "cartItems")
  ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);
}
