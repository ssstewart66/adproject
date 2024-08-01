package sg.nus.iss.javaspring.adprojrct.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Services.CategoryService;
import sg.nus.iss.javaspring.adprojrct.Services.TransactionService;
import sg.nus.iss.javaspring.adprojrct.Services.UserService;

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
    @Autowired
    private UserService userService;

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

    @GetMapping("/categories/{type}")
    public ResponseEntity<List<Category>> getCategoriesByType(@PathVariable int type) {
        List<Category> categories = categoryService.getCategoriesByType(type);
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

    @GetMapping("/transaction_user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionBycuserId(@PathVariable Integer userId) {
        Optional<List<Transaction>> Transactions = transactionService.getTransactionsByUserId(userId);
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

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        Optional<User> inuser = userService.findUserById(userId);
        if (inuser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(inuser.get());
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Integer userId) {
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User updatedUser = userService.updateUser(user, userId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/deleteuser/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUserById(userId);
    }
}

