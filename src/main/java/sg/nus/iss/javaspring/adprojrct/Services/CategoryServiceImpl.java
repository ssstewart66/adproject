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
    public Category addCategory(Category category, Integer userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            category.setUser(optionalUser.get());
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    @Transactional
    public Category updateCategory(Category category, Integer id){
        return categoryRepository.findById(id).map(cat ->{
            cat.setName(category.getName());
            cat.setBudget(category.getBudget());
            cat.setType(category.getType());
            return categoryRepository.save(cat);
        }).orElseThrow(()->new RuntimeException("Category not found"));
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }
}
