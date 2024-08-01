package sg.nus.iss.javaspring.adprojrct.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;
import sg.nus.iss.javaspring.adprojrct.Services.CategoryService;
import sg.nus.iss.javaspring.adprojrct.Services.TransactionService;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = "/Admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/dashboard")
    public String dashboard() {
        return "adminDashboard";
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{userId}")
    public ResponseEntity<List<Category>> getCategoriesCreatedByAdmin(@PathVariable Integer userId) {
        List<Category> categories = categoryService.getCategoriesByUserId(userId);
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/user")
    public ResponseEntity<List<Category>> getCategoriesCreatedByUser() {
        List<Category> categories = categoryService.getCategoriesNotByUserId(1);
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

    @PutMapping("/update/{catId}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable Integer catId) {
        Optional<Category> optionalCategory = categoryService.getCategoryById(catId);
        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Category updatedCategory = categoryService.updateCategory(category, catId);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{catId}")
    public void deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategory(catId);
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

    @GetMapping("/transaction/{catId}")
    public ResponseEntity<List<Transaction>> getTransactionBycatId(@PathVariable Integer catId) {
        Optional<List<Transaction>> Transactions = transactionService.getTransactionsByCategoryId(catId);
        if (Transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            List<Transaction> transactions = Transactions.get();
            return ResponseEntity.ok(transactions);
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transaction_detail/{transcationId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Integer transcationId) {
        Optional<Transaction> transcation = transactionService.getTransactionById(transcationId);
        if (transcation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return ResponseEntity.ok(transcation.get());
        }
    }
}

