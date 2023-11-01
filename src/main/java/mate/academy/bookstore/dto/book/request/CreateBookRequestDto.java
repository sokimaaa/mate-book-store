package mate.academy.bookstore.dto.book.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ISBN;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {

  @NotNull(message = "Title cannot be null.")
  @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters.")
  private String title;

  @NotNull(message = "Author cannot be null.")
  @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters.")
  private String author;

  @NotNull(message = "ISBN cannot be null.")
  @Size(min = 1, max = 13, message = "ISBN must be between 1 and 13 characters.")
  @Schema(example = "978-3-16-148410-0")
  @ISBN
  private String isbn;

  @NotNull(message = "Price cannot be null.")
  @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
  private BigDecimal price;

  @Size(max = 2000, message = "Description can't be longer than 2000 characters.")
  private String description;
  private String coverImage;
  private Set<Long> categoryIds;
}
