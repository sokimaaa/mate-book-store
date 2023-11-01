package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.book.request.BookSearchParametersDto;
import mate.academy.bookstore.dto.book.request.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.response.BookDto;
import mate.academy.bookstore.dto.book.response.BookDtoWithoutCategoryIds;
import org.springframework.data.domain.Pageable;

public interface BookService {
  List<BookDto> findAll(Pageable pageable);

  BookDto getById(Long id);

  BookDto save(CreateBookRequestDto bookDto);

  List<BookDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable);

  List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId);

  BookDto update(Long bookId, CreateBookRequestDto bookDto);

  void deleteById(Long id);
}
