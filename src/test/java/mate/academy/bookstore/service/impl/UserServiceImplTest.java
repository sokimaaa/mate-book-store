package mate.academy.bookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.bookstore.dto.user.request.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.response.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {

  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserMapper userMapper;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper);
  }

  @Test
  @DisplayName("""
    Expected to receive a RegistrationException when registering a user with the same email twice
    """)
  public void testRegister_UserAlreadyExists_ThrowsRegistrationException() {
    // Given
    UserRegistrationRequestDto request = new UserRegistrationRequestDto();
    request.setEmail("test@example.com");

    User existingUser = new User();
    existingUser.setEmail("test@example.com");

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

    // When and Then
    assertThrows(RegistrationException.class, () -> userService.register(request));
  }

  @Test
  @DisplayName("Successful use case")
  public void testRegister_SuccessfulRegistration_ReturnsUserResponseDto() throws RegistrationException {
    // Given
    UserRegistrationRequestDto request = new UserRegistrationRequestDto();
    request.setEmail("new@example.com");

    User newUser = new User();
    newUser.setEmail("new@example.com");

    UserResponseDto responseDto = new UserResponseDto();
    responseDto.setEmail("new@example.com");

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(newUser);
    when(userMapper.toUserResponse(any(User.class))).thenReturn(responseDto);

    // When
    UserResponseDto result = userService.register(request);

    // Then
    assertEquals("new@example.com", result.getEmail());
  }
}
