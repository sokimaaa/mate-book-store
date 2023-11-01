package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.user.request.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.response.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;

public interface UserService {
  UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;
}
