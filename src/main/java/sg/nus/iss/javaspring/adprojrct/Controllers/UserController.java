package sg.nus.iss.javaspring.adprojrct.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Services.CategoryService;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = "/User")
public class UserController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/dashboard")
    public String dashboard(Model model) {
        return "userDashboard";
    }

    @GetMapping(value = "/budget/{userId}")
    public ResponseEntity<List<Category>> getBudgetsById(@PathVariable Integer userId) {
        return ResponseEntity.ok(categoryService.getCategoriesByUserId(userId));
    }

    @PostMapping("/budget/add/{userId}")
    public ResponseEntity<?> createCategory(@RequestBody Category category, @PathVariable Integer userId) {
        try {
            category.setType(1);
            Category newCategory = categoryService.addCategory(category, userId);
            return ResponseEntity.ok(newCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/budget/update/{catId}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable Integer catId) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, catId);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/budget/delete/{catId}")
    public void deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategory(catId);
    }

}
