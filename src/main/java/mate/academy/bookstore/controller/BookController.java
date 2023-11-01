package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.book.request.BookSearchParametersDto;
import mate.academy.bookstore.dto.book.request.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.response.BookDto;
import mate.academy.bookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @GetMapping
  @Operation(summary = "Get all books", description = "Get a list of all books available")
  public List<BookDto> getAll(Pageable pageable) {
    return bookService.findAll(pageable);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a book by id", description = "Get details of a book by its id")
  public BookDto getById(@PathVariable Long id) {
    return bookService.getById(id);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a new book", description = "Create a new book. Requires admin role.")
  public BookDto create(@RequestBody CreateBookRequestDto bookDto) {
    return bookService.save(bookDto);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update a book", description = "Update an existing book by id. Requires admin role.")
  public BookDto update(@PathVariable Long id, @RequestBody CreateBookRequestDto bookDto) {
    return bookService.update(id, bookDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete a book", description = "Delete a book by id. Requires admin role.")
  public void deleteById(@PathVariable Long id) {
    bookService.deleteById(id);
  }

  @GetMapping("/search")
  @Operation(summary = "Search for books", description = "Search for books using search parameters")
  public List<BookDto> search(
    BookSearchParametersDto searchParameters,
    Pageable pageable
  ) {
    return bookService.searchBooks(searchParameters, pageable);
  }
}
