package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.user.response.UserResponseDto;
import mate.academy.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface UserMapper {
  UserResponseDto toUserResponse(User user);
}
