package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.cart.response.CartItemDto;
import mate.academy.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
  @Mapping(source = "book.id", target = "bookId")
  @Mapping(source = "book.title", target = "bookTitle")
  CartItemDto toDto(CartItem cartItem);

  @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
  CartItem toEntity(CartItemDto cartItemDto);
}
