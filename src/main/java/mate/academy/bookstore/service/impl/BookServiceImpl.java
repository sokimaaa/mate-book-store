package mate.academy.bookstore.service.impl;

import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.book.request.BookSearchParametersDto;
import mate.academy.bookstore.dto.book.request.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.response.BookDto;
import mate.academy.bookstore.dto.book.response.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.CategoryRepository;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.book.BookSpecificationFactory;
import mate.academy.bookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;
  private final BookMapper bookMapper;

  private final BookSpecificationFactory bookSpecificationFactory;

  //@Transactional is used here to fetch books with categories to avoid LazyInitializationException
  @Transactional
  public List<BookDto> findAll(Pageable pageable) {
    return bookRepository.findAll(pageable).stream()
      .map(bookMapper::toDto)
      .toList();
  }

  public BookDto getById(Long id) {
    return bookMapper.toDto(bookRepository.findById(id)
      .orElseThrow(EntityNotFoundException.supplier("Can't find category by id " + id)));
  }

  public BookDto save(CreateBookRequestDto bookDto) {
    Book book = bookMapper.toEntity(bookDto);
    List<Category> categories = categoryRepository.findAllById(bookDto.getCategoryIds());
    book.setCategories(new HashSet<>(categories));
    Book savedBook = bookRepository.save(book);
    return bookMapper.toDto(savedBook);
  }

  @Transactional
  public List<BookDto> searchBooks(
    BookSearchParametersDto searchParameters, Pageable pageable
  ) {
    Specification<Book> spec = bookSpecificationFactory.createSpecification(searchParameters);

    List<Book> books = bookRepository.findAll(spec, pageable).toList();
    return books.stream()
      .map(bookMapper::toDto)
      .toList();
  }

  public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId) {
    return bookRepository.findAllByCategoryId(categoryId).stream()
      .map(bookMapper::toDtoWithoutCategories)
      .toList();
  }

  @Override
  public BookDto update(Long bookId, CreateBookRequestDto bookDto) {
    if (!bookRepository.existsById(bookId)) {
      throw new EntityNotFoundException("Can't found book with id: " + bookId);
    }
    Book book = bookMapper.toEntity(bookDto);
    book.setId(bookId);
    Book updatedBook = bookRepository.save(book);
    return bookMapper.toDto(updatedBook);
  }

  @Override
  public void deleteById(Long id) {
    bookRepository.deleteById(id);
  }
}
