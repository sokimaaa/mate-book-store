package mate.academy.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.request.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.response.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.UserRepository;
import mate.academy.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new RegistrationException("Unable to complete registration.");
    }

    User user = new User();
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setEmail(request.getEmail());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setShippingAddress(request.getShippingAddress());
    User savedUser = userRepository.save(user);
    return userMapper.toUserResponse(savedUser);
  }
}
