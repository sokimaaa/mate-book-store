package mate.academy.bookstore.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {
  private Long id;
  @NotNull(message = "Name cannot be null.")
  @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters.")
  private String name;
  @Size(max = 2000, message = "Description can't be longer than 2000 characters.")
  private String description;
}
