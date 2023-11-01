package mate.academy.bookstore.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstore.dto.book.request.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.response.BookDto;
import mate.academy.bookstore.dto.book.response.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.CategoryRepository;
import mate.academy.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

  @InjectMocks
  private BookServiceImpl bookService;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private BookMapper bookMapper;

  @Test
  @DisplayName("Verify findAll() works")
  public void findAll_ShouldReturnAllBooks() {
    // Given
    Book book = new Book();
    book.setId(1L);
    book.setTitle("Test Book");

    BookDto bookDto = new BookDto();
    bookDto.setId(1L);
    bookDto.setTitle("Test Book");

    Pageable pageable = PageRequest.of(0, 10);
    List<Book> books = Collections.singletonList(book);
    Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

    when(bookRepository.findAll(pageable)).thenReturn(bookPage);
    when(bookMapper.toDto(book)).thenReturn(bookDto);

    // When
    List<BookDto> bookDtos = bookService.findAll(pageable);

    // Then
    assertThat(bookDtos).hasSize(1);
    assertThat(bookDtos.get(0).getId()).isEqualTo(bookDto.getId());
    assertThat(bookDtos.get(0).getTitle()).isEqualTo(bookDto.getTitle());

    // Verify no further interactions with the mocks
    verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
  }

  @Test
  public void getById_ShouldReturnBook() {
    // Given
    Long id = 1L;
    Book book = new Book();
    BookDto bookDto = new BookDto();
    Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
    Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

    // When
    BookDto result = bookService.getById(id);

    // Then
    assertEquals(bookDto, result);
  }

  @Test
  public void save_ShouldSaveAndReturnBook() {
    // Given
    CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
    BookDto bookDto = new BookDto();
    Book book = new Book();
    Mockito.when(bookMapper.toEntity(createBookRequestDto)).thenReturn(book);
    Mockito.when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(book);
    Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

    // When
    BookDto result = bookService.save(createBookRequestDto);

    // Then
    assertEquals(bookDto, result);
  }

  @Test
  public void update_ShouldUpdateAndReturnBook() {
    // Given
    Long bookId = 1L;
    BookDto bookDto = new BookDto();
    CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
    Book book = new Book();

    Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
    Mockito.when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(book);
    Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);
    Mockito.when(bookMapper.toEntity(createBookRequestDto)).thenReturn(book);

    // When
    BookDto result = bookService.update(bookId, createBookRequestDto);

    // Then
    assertEquals(bookDto, result);
  }

  @Test
  public void delete_ShouldDeleteBook() {
    // Given
    Long bookId = 1L;

    Mockito.doNothing().when(bookRepository).deleteById(bookId);

    // When
    bookService.deleteById(bookId);

    // Then
    Mockito.verify(bookRepository).deleteById(bookId);
  }

  @Test
  public void findBooksByCategoryId_ShouldReturnBooksOfACategory() {
    // Given
    Category category = new Category();
    category.setName("Science");
    Book book = new Book();
    book.setAuthor("Bob");
    book.setPrice(BigDecimal.valueOf(100));
    book.setCategories(Set.of(category));
    BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
    bookDto.setAuthor("Bob");
    bookDto.setPrice(BigDecimal.valueOf(100));
    Long categoryId = 1L;

    Mockito.when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(book));
    Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);

    // When
    List<BookDtoWithoutCategoryIds> result = bookService.findBooksByCategoryId(categoryId);

    // Then
    assertEquals(Collections.singletonList(bookDto), result);

    // Verify no more interactions with the mocks
    Mockito.verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
  }
}
