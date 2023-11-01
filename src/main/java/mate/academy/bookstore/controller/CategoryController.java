package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.book.response.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.service.BookService;
import mate.academy.bookstore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Endpoints for managing categories")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;
  private final BookService bookService;

  @Operation(summary = "Create a new category")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Category created successfully",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = CategoryDto.class))}),
    @ApiResponse(responseCode = "400", description = "Invalid request",
      content = @Content)
  })
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public CategoryDto create(@RequestBody CategoryDto categoryDto) {
    return categoryService.save(categoryDto);
  }

  @GetMapping
  @Operation(summary = "Get all categories")
  public List<CategoryDto> getAll() {
    return categoryService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a category by its ID")
  public CategoryDto getById(@PathVariable Long id) {
    return categoryService.getById(id);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a category by its ID")
  public CategoryDto update(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
    return categoryService.update(id, categoryDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a category by its ID")
  public void deleteById(@PathVariable Long id) {
    categoryService.deleteById(id);
  }

  @GetMapping("/{id}/books")
  @Operation(summary = "Get books by category ID")
  public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
    return bookService.findBooksByCategoryId(id);
  }
}
