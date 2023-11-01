package mate.academy.bookstore.repository.book;

import mate.academy.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecification {
  public static Specification<Book> withTitle(String title) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
      .like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
  }

  public static Specification<Book> withAuthor(String author) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
      .like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%");
  }

  public static Specification<Book> withIsbn(String isbn) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
      .like(criteriaBuilder.lower(root.get("isbn")), "%" + isbn.toLowerCase() + "%");
  }
}
