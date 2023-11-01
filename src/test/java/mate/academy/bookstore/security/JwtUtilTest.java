package mate.academy.bookstore.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtUtilTest {

  @Autowired
  private JwtUtil jwtUtil;

  @Test
  void generateToken_Ok() {
    String username = "TestUser";
    String token = jwtUtil.generateToken(username);

    assertThat(token).isNotEmpty();
  }

  @Test
  void validateToken() {
    String username = "TestUser";
    String token = jwtUtil.generateToken(username);

    assertThat(jwtUtil.validateToken(token)).isTrue();
  }

  @Test
  void getUsernameFromToken() {
    String username = "TestUser";
    String token = jwtUtil.generateToken(username);

    assertThat(jwtUtil.getUsernameFromToken(token)).isEqualTo(username);
  }
}
