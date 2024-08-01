package sg.nus.iss.javaspring.adprojrct.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.DTO.CategoryDTO;
import sg.nus.iss.javaspring.adprojrct.DTO.TransactionDTO;
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
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{userId}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesCreatedByAdmin(@PathVariable Integer userId) {
        List<CategoryDTO> categories = categoryService.getCategoriesByUserId(userId);
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/user")
    public ResponseEntity<List<CategoryDTO>> getCategoriesCreatedByUser() {
        List<CategoryDTO> categories = categoryService.getCategoriesNotByUserId(1);
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDto, @PathVariable Integer userId) {
        CategoryDTO newCategory = categoryService.addCategory(categoryDto, userId);
        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/update/{catId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDto, @PathVariable Integer catId) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryDto, catId);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{catId}")
    public void deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategory(catId);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Integer id) {
        Optional<CategoryDTO> optionalCategory = categoryService.getCategoryById(id);
        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(optionalCategory.get());
        }
    }

    @GetMapping("/transaction/{catId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionBycatId(@PathVariable Integer catId) {
        Optional<List<TransactionDTO>> transactions = transactionService.getTransactionsByCategoryId(catId);
        if (transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(transactions.get());
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transaction_detail/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Integer transactionId) {
        Optional<TransactionDTO> transaction = transactionService.getTransactionById(transactionId);
        if (transaction.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(transaction.get());
        }
    }
}