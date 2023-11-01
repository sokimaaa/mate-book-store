package mate.academy.bookstore.security;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.request.UserLoginRequestDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public String authenticate(UserLoginRequestDto request) {
    final Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.email(), request.password())
    );

    return jwtUtil.generateToken(authentication.getName());
  }
}
