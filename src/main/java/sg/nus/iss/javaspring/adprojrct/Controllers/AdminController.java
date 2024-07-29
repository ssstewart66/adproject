package sg.nus.iss.javaspring.adprojrct.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Services.CategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/Admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/dashboard")
    public String dashboard() {
        return "adminDashboard";
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getcategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<Category> createCategory(@RequestBody Category category, @PathVariable Integer userId) {
        Category newCategory = categoryService.addCategory(category, userId);
        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/update/{id}")
    public Category updateCategory(@RequestBody Category category, @PathVariable Integer id){
        return categoryService.updateCategory(category, id);
    }

    @DeleteMapping("delete/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> optionalCategory = categoryService.getCategoryById(id);
        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Category category = optionalCategory.get();
            return ResponseEntity.ok(category);
        }
    }
}

