package mate.academy.bookstore.mapper;

import java.util.Optional;
import java.util.stream.Collectors;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.book.request.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.response.BookDto;
import mate.academy.bookstore.dto.book.response.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
  @Mapping(target = "categoryIds", ignore = true)
  BookDto toDto(Book book);

  BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

  @AfterMapping
  default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
    if (book.getCategories() != null) {
      bookDto.setCategoryIds(book.getCategories().stream()
        .map(Category::getId)
        .collect(Collectors.toSet()));
    }
  }

  @Mapping(target = "categories", ignore = true)
  Book toEntity(CreateBookRequestDto bookDto);

  @Named("bookFromId")
  default Book bookFromId(Long id) {
    return Optional.ofNullable(id)
      .map(Book::new)
      .orElse(null);
  }

  @AfterMapping
  default void setCategories(@MappingTarget Book book, CreateBookRequestDto bookDto) {
    book.setCategories(bookDto.getCategoryIds().stream()
      .map(Category::new)
      .collect(Collectors.toSet()));
  }
}
