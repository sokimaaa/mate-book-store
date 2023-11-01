package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.CategoryRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
  CategoryDto toDto(Category category);

  Category toEntity(CategoryDto categoryDto);

  @Named("categoryFromId")
  default Category categoryFromId(Long id, @Context CategoryRepository categoryRepository) {
    if (id == null) {
      return null;
    }
    return categoryRepository.findById(id)
      .orElseThrow(EntityNotFoundException.supplier("Can't find category by id " + id));
  }
}
