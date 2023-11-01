package mate.academy.bookstore.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
  @NotEmpty
  @Size(min = 8, max = 20)
  @Email
  String email,
  @NotEmpty
  @Size(min = 8, max = 20)
  // Create and use custom validation if you want
  String password
) {
}
