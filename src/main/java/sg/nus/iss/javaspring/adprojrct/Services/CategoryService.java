package sg.nus.iss.javaspring.adprojrct.Services;

import sg.nus.iss.javaspring.adprojrct.Models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(int id);
    Category addCategory(Category category, Integer userId);
    Category updateCategory(Category category, Integer id);
    void deleteCategory(int id);
    List<Category> getCategoriesNotByUserId(int userId);
    List<Category> getCategoriesByUserId(int userId);
}
