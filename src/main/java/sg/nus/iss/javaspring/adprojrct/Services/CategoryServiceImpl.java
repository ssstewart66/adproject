package sg.nus.iss.javaspring.adprojrct.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.javaspring.adprojrct.DTO.CategoryDTO;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Repositories.CategoryRepository;
import sg.nus.iss.javaspring.adprojrct.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<CategoryDTO> getCategoryById(int id) {
        return categoryRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO addCategory(CategoryDTO categoryDto, Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            // 检查是否存在相同名称的类别
            Optional<Category> existingCategory = categoryRepository.findByNameAndUserId(categoryDto.getName(), userId);
            if (existingCategory.isPresent()) {
                throw new IllegalArgumentException("Category with the same name already exists");
            }

            Category category = new Category();
            category.setName(categoryDto.getName());
            category.setBudget(categoryDto.getBudget());
            category.setType(categoryDto.getType());
            category.setUser(optionalUser.get());

            Category savedCategory = categoryRepository.save(category);
            return convertToDto(savedCategory);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDto, Integer id) {
        return categoryRepository.findById(id).map(cat -> {
            if (!cat.getName().equals(categoryDto.getName())) {
                // 检查是否存在相同名称的类别
                Optional<Category> existingCategory = categoryRepository.findByNameAndUserId(categoryDto.getName(), cat.getUser().getId());
                if (existingCategory.isPresent()) {
                    throw new IllegalArgumentException("Category with the same name already exists");
                }
            }
            cat.setName(categoryDto.getName());
            cat.setBudget(categoryDto.getBudget());
            cat.setType(categoryDto.getType());
            Category updatedCategory = categoryRepository.save(cat);
            return convertToDto(updatedCategory);
        }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> getCategoriesByUserId(int userId) {
        return categoryRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getCategoriesNotByUserId(int userId) {
        return categoryRepository.findByUserIdNot(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CategoryDTO convertToDto(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setBudget(category.getBudget());
        dto.setType(category.getType());
        dto.setUserId(category.getUser().getId());
        return dto;
    }
}