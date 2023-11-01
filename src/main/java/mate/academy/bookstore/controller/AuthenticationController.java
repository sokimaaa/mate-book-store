package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.request.UserLoginRequestDto;
import mate.academy.bookstore.dto.user.request.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.response.UserLoginResponseDto;
import mate.academy.bookstore.dto.user.response.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.security.AuthenticationService;
import mate.academy.bookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final UserService userService;
  private final AuthenticationService authenticationService;

  @PostMapping("/registration")
  @Operation(summary = "Register a new user")
  public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request) throws RegistrationException {
    return userService.register(request);
  }

  @PostMapping("/login")
  @Operation(summary = "Authenticate a user")
  public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto request) {
    String token = authenticationService.authenticate(request);
    return new UserLoginResponseDto(token);
  }
}
