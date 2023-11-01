package mate.academy.bookstore.repository.book;

import mate.academy.bookstore.dto.book.request.BookSearchParametersDto;
import mate.academy.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationFactory {
  public Specification<Book> createSpecification(BookSearchParametersDto searchParameters) {
    Specification<Book> spec = Specification.where(null);

    if (searchParameters.title() != null && !searchParameters.title().isEmpty()) {
      spec = spec.and(BookSpecification.withTitle(searchParameters.title()));
    }

    if (searchParameters.author() != null && !searchParameters.author().isEmpty()) {
      spec = spec.and(BookSpecification.withAuthor(searchParameters.author()));
    }

    if (searchParameters.isbn() != null && !searchParameters.isbn().isEmpty()) {
      spec = spec.and(BookSpecification.withIsbn(searchParameters.isbn()));
    }
    return spec;
  }
}
