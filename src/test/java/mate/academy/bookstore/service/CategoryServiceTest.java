package mate.academy.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.CategoryRepository;
import mate.academy.bookstore.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @InjectMocks
  private CategoryServiceImpl categoryService;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private CategoryMapper categoryMapper;

  @Test
  @DisplayName("Verify findAll() works for categories")
  public void findAll_ShouldReturnAllCategories() {
    // Given
    Category category = new Category();
    category.setId(1L);
    category.setName("TestCategory");

    CategoryDto categoryDto = new CategoryDto();
    categoryDto.setId(1L);
    categoryDto.setName("TestCategory");

    List<Category> categories = Collections.singletonList(category);

    when(categoryRepository.findAll()).thenReturn(categories);
    when(categoryMapper.toDto(category)).thenReturn(categoryDto);

    // When
    List<CategoryDto> categoryDtos = categoryService.findAll();

    // Then
    assertThat(categoryDtos).hasSize(1);
    assertThat(categoryDtos.get(0).getId()).isEqualTo(categoryDto.getId());
    assertThat(categoryDtos.get(0).getName()).isEqualTo(categoryDto.getName());

    verifyNoMoreInteractions(categoryRepository, categoryMapper);
  }

  @Test
  @DisplayName("Verify findById() works for categories")
  public void getById_ExistingId_ShouldReturnCategory() {
    // Given
    Long id = 1L;
    Category category = new Category();
    CategoryDto categoryDto = new CategoryDto();
    Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
    Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);

    // When
    CategoryDto result = categoryService.getById(id);

    // Then
    assertEquals(categoryDto, result);
  }

  @Test
  @DisplayName("Verify findById() throw and exception if there is no such category")
  public void getById_NonExistingId_ShouldThrowException() {
    // Given
    Long id = 1L;
    Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.empty());

    // Expect exception
    assertThrows(EntityNotFoundException.class, () -> {
      categoryService.getById(id);
    });
  }

  @Test
  @DisplayName("Verify saving a new category works as expected")
  public void save_ShouldReturnSavedCategory() {
    // Given
    CategoryDto inputCategoryDto = new CategoryDto();
    Category category = new Category();
    Category savedCategory = new Category();
    CategoryDto savedCategoryDto = new CategoryDto();

    when(categoryMapper.toEntity(inputCategoryDto)).thenReturn(category);
    when(categoryRepository.save(category)).thenReturn(savedCategory);
    when(categoryMapper.toDto(savedCategory)).thenReturn(savedCategoryDto);

    // When
    CategoryDto result = categoryService.save(inputCategoryDto);

    // Then
    assertEquals(savedCategoryDto, result);
  }

  @Test
  @DisplayName("Verify updating an existing category works as expected")
  public void update_ExistingId_ShouldReturnUpdatedCategory() {
    // Given
    Long id = 1L;
    Category existingCategory = new Category();
    CategoryDto updatedCategoryDto = new CategoryDto();
    Category updatedCategory = new Category();
    CategoryDto resultCategoryDto = new CategoryDto();

    when(categoryRepository.existsById(id)).thenReturn(true);
    when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
    when(categoryMapper.toEntity(updatedCategoryDto)).thenReturn(existingCategory);
    when(categoryMapper.toDto(updatedCategory)).thenReturn(resultCategoryDto);

    // When
    CategoryDto result = categoryService.update(id, updatedCategoryDto);

    // Then
    assertEquals(resultCategoryDto, result);
  }

  @Test
  @DisplayName("Verify updating an non-existing category cause an exception")
  public void update_NonExistingId_ShouldThrowException() {
    // Given
    Long id = 1L;
    CategoryDto updatedCategoryDto = new CategoryDto();
    when(categoryRepository.existsById(id)).thenReturn(false);

    // Expect exception
    assertThrows(EntityNotFoundException.class, () -> {
      categoryService.update(id, updatedCategoryDto);
    });
  }

  @Test
  @DisplayName("Verify deleteById works as expected")
  public void delete_ExistingId_ShouldNotThrowException() {
    // Given
    Long id = 1L;

    // This method does not return a value, so it's more about asserting
    // that no exception is thrown
    assertDoesNotThrow(() -> {
      categoryService.deleteById(id);
    });
    verify(categoryRepository).deleteById(id);
  }
}
