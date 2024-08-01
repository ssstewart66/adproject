package sg.nus.iss.javaspring.adprojrct.Services;

import sg.nus.iss.javaspring.adprojrct.DTO.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    Optional<CategoryDTO> getCategoryById(int id);
    CategoryDTO addCategory(CategoryDTO categoryDto, Integer userId);
    CategoryDTO updateCategory(CategoryDTO categoryDto, Integer id);
    void deleteCategory(int id);
    List<CategoryDTO> getCategoriesNotByUserId(int userId);
    List<CategoryDTO> getCategoriesByUserId(int userId);
}