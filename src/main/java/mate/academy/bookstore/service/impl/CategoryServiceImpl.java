package mate.academy.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.CategoryRepository;
import mate.academy.bookstore.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public List<CategoryDto> findAll() {
    return categoryRepository.findAll().stream()
      .map(categoryMapper::toDto)
      .toList();
  }

  public CategoryDto save(CategoryDto categoryDto) {
    Category category = categoryMapper.toEntity(categoryDto);
    Category savedCategory = categoryRepository.save(category);
    return categoryMapper.toDto(savedCategory);
  }

  public CategoryDto getById(Long id) {
    return categoryRepository.findById(id)
      .map(categoryMapper::toDto)
      .orElseThrow(EntityNotFoundException.supplier("Category not found with ID: " + id));
  }

  public CategoryDto update(Long id, CategoryDto categoryDto) {
    if (!categoryRepository.existsById(id)) {
      throw new EntityNotFoundException("Category not found with ID: " + id);
    }
    Category category = categoryMapper.toEntity(categoryDto);
    category.setId(id);
    Category updatedCategory = categoryRepository.save(category);
    return categoryMapper.toDto(updatedCategory);
  }

  public void deleteById(Long id) {
    categoryRepository.deleteById(id);
  }
}
