package sg.nus.iss.javaspring.adprojrct.Services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Repositories.CategoryRepository;
import sg.nus.iss.javaspring.adprojrct.Repositories.TransactionRepository;
import sg.nus.iss.javaspring.adprojrct.Repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    @Override
    public Optional<List<Transaction>> getTransactionsByCategoryId(int categoryId){
        return transactionRepository.findByCategoryId(categoryId);
    }

    @Override
    public Optional<Transaction> getTransactionById(int transactionId){
        return transactionRepository.findById(transactionId);
    }

    @Override
    public Optional<List<Transaction>> getTransactionsByUserId(int userId){
        return transactionRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Transaction addTransaction(Transaction transaction, int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Category> optionalCategory = categoryRepository.findById(transaction.getCategory().getId());

        if (optionalUser.isPresent() && optionalCategory.isPresent()) {
            transaction.setUser(optionalUser.get());
            transaction.setCategory(optionalCategory.get());
            transaction.setUpdated_at(LocalDate.now());

            return transactionRepository.save(transaction);
        } else {
            throw new EntityNotFoundException("User or Category not found");
        }
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Transaction transaction, int transactionId) {
        return transactionRepository.findById(transactionId).map(existingTransaction -> {
            existingTransaction.setAmount(transaction.getAmount());
            existingTransaction.setDescription(transaction.getDescription());
            existingTransaction.setCreated_at(transaction.getCreated_at());
            existingTransaction.setUpdated_at(LocalDate.now());
            return transactionRepository.save(existingTransaction);
        }).orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + transactionId));
    }

    @Override
    @Transactional
    public void deleteTransaction(int transactionId) {
        if (transactionRepository.existsById(transactionId)) {
            transactionRepository.deleteById(transactionId);
        } else {
            throw new EntityNotFoundException("Transaction not found with ID: " + transactionId);
        }
    }


}
