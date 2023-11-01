package mate.academy.bookstore.dto.book.request;

public record BookSearchParametersDto(String title, String author, String isbn) {
  // Add more search parameters as needed
}
