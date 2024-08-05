package sg.nus.iss.javaspring.adprojrct.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Repositories.CategoryRepository;
import sg.nus.iss.javaspring.adprojrct.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category addCategory(Category category, Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            // 检查是否存在相同名称的类别
            Optional<Category> existingCategory = categoryRepository.findByNameAndUserId(category.getName(), userId);
            if (existingCategory.isPresent()) {
                throw new IllegalArgumentException("Category with the same name already exists");
            }

            category.setUser(optionalUser.get());
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    @Transactional
    public Category updateCategory(Category category, Integer id) {
        return categoryRepository.findById(id).map(cat -> {
            if (!cat.getName().equals(category.getName())) {
                // 检查是否存在相同名称的类别
                Optional<Category> existingCategory = categoryRepository.findByNameAndUserId(category.getName(), cat.getUser().getId());
                if (existingCategory.isPresent()) {
                    throw new IllegalArgumentException("Category with the same name already exists");
                }
            }
            cat.setName(category.getName());
            cat.setBudget(category.getBudget());
            if (category.getType() == 0) {
                cat.setType(0);
            } else if (category.getType() == 1) {
                cat.setType(1);
            }
            return categoryRepository.save(cat);
        }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getCategoriesByUserId(int userId) {
        return categoryRepository.findByUserId(userId);
    }

    @Override
    public List<Category> getCategoriesByType(int type){
        return categoryRepository.findByType(type);
    }


    @Override
    public double getTotalBudgetByUserId(int userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);
        return categories.stream()
                .mapToDouble(Category::getBudget)
                .sum();
    }
}
