package sg.nus.iss.javaspring.adprojrct.Services;

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import io.reactivex.Flowable;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Repositories.CategoryRepository;
import sg.nus.iss.javaspring.adprojrct.Repositories.TransactionRepository;
import sg.nus.iss.javaspring.adprojrct.Repositories.UserRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<Transaction> findTransactionsByOrderDateAtDesc(int userId) {
        return transactionRepository.findTransactionsByOrderDateAtDesc(userId);
    }

    @Override
    @Transactional
    public Transaction addTransaction(Transaction transaction, int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        System.out.println(optionalUser.isPresent());
        System.out.println(transaction.getCategory().getId());
        Optional<Category> optionalCategory = categoryRepository.findById(transaction.getCategory().getId());
        System.out.println(optionalCategory.isPresent());

        if (optionalUser.isPresent() && optionalCategory.isPresent()) {
            transaction.setUser(optionalUser.get());
            transaction.setCategory(optionalCategory.get());
            transaction.setUpdated_at(LocalDateTime.now());
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
            existingTransaction.setUpdated_at(LocalDateTime.now());
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


    @Override
    public double getTotalSpendingLastMonth(int userId) {
        return getTotalSpending(userId, LocalDate.now().minusMonths(1), LocalDate.now());
    }

    @Override
    public double getTotalSpendingPreviousMonth(int userId) {
        return getTotalSpending(userId, LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1));
    }

    @Override
    public double getTotalSpendingToday(int userId) {
        return getTotalSpending(userId, LocalDate.now(), LocalDate.now());
    }

    @Override
    public double getTotalSpendingLastWeek(int userId) {
        return getTotalSpending(userId, LocalDate.now().minusWeeks(1), LocalDate.now());
    }

    @Override
    public double getTotalSpendingLastYear(int userId) {
        return getTotalSpending(userId, LocalDate.now().minusYears(1), LocalDate.now());
    }

    @Override
    public double getTotalSpendingCurrentMonth(int userId){
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1);
        return getTotalSpending(userId, startOfMonth, endOfMonth);
    }

    @Override
    public List<Map<String,Object>> getTotalSpendingByCategoryForCurrentMonth(int userId){
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1);
        List<Object[]> results = transactionRepository.findTotalSpendingByCategoryForCurrentMonth(userId, startOfMonth, endOfMonth);
        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("categoryId", result[0]);
            map.put("totalSpending", result[1]);
            return map;
        }).collect(Collectors.toList());
    }


    private double getTotalSpending(int userId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findTransactionsByUserIdAndDateRange(userId, startDate, endDate);

        double totalSpending = 0.0;
        for (Transaction transaction : transactions) {
            totalSpending += transaction.getAmount();
        }

        return totalSpending;
    }

    @Override
    public List<Object[]> getTotalSpendingByCategoryForCurrent(int userId){
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        return transactionRepository.findTotalSpendingByCategoryForCurrentMonth(userId, startOfMonth, endOfMonth);
    }

    @Override
    public List<Object[]> getAverageAmountPerCategory() {
        return transactionRepository.findAverageAmountPerCategory();
    }

//    @Override
//    public void deleteTransaction(Integer transactionId) {
//        if (!transactionRepository.existsById(transactionId)) {
//            throw new EntityNotFoundException("Transaction not found with ID: " + transactionId);
//        }
//        transactionRepository.deleteById(transactionId);
//    }
//
//    @Override
//    public Transaction updateTransaction(Transaction transaction, Integer transactionId) {
//        return transactionRepository.findById(transactionId).map(existingTransaction -> {
//            existingTransaction.setAmount(transaction.getAmount());
//            existingTransaction.setDescription(transaction.getDescription());
//            existingTransaction.setCreated_at(transaction.getCreated_at());
//            existingTransaction.setUpdated_at(LocalDateTime.now());
//            return transactionRepository.save(existingTransaction);
//        }).orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + transactionId));
//    }


}
