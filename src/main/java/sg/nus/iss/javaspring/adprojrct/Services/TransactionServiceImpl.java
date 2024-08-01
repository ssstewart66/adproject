package sg.nus.iss.javaspring.adprojrct.Services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.javaspring.adprojrct.DTO.TransactionDTO;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Repositories.CategoryRepository;
import sg.nus.iss.javaspring.adprojrct.Repositories.TransactionRepository;
import sg.nus.iss.javaspring.adprojrct.Repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<List<TransactionDTO>> getTransactionsByCategoryId(int categoryId) {
        return transactionRepository.findByCategoryId(categoryId).map(transactions ->
                transactions.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @Override
    public Optional<TransactionDTO> getTransactionById(int transactionId) {
        return transactionRepository.findById(transactionId).map(this::convertToDto);
    }

    @Override
    public List<TransactionDTO> findTransactionsByUserId(int userId) {
        return transactionRepository.findTransactionsByUserId(userId)
                .map(transactions -> transactions.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new EntityNotFoundException("No transactions found for user with ID: " + userId));
    }

    @Override
    @Transactional
    public TransactionDTO addTransaction(TransactionDTO transactionDto, int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Category> optionalCategory = categoryRepository.findById(transactionDto.getCategoryId());

        if (optionalUser.isPresent() && optionalCategory.isPresent()) {
            Transaction transaction = new Transaction();
            transaction.setUser(optionalUser.get());
            transaction.setCategory(optionalCategory.get());
            transaction.setAmount(transactionDto.getAmount());
            transaction.setDescription(transactionDto.getDescription());
            transaction.setCreated_at(LocalDate.now());
            transaction.setUpdated_at(LocalDate.now());

            return convertToDto(transactionRepository.save(transaction));
        } else {
            throw new EntityNotFoundException("User or Category not found");
        }
    }

    @Override
    @Transactional
    public TransactionDTO updateTransaction(TransactionDTO transactionDto, int transactionId) {
        return transactionRepository.findById(transactionId).map(existingTransaction -> {
            existingTransaction.setAmount(transactionDto.getAmount());
            existingTransaction.setDescription(transactionDto.getDescription());
            existingTransaction.setUpdated_at(LocalDate.now());
            return convertToDto(transactionRepository.save(existingTransaction));
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

    private TransactionDTO convertToDto(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setCreatedAt(transaction.getCreated_at());
        dto.setUpdatedAt(transaction.getUpdated_at());
        dto.setCategoryId(transaction.getCategory().getId());
        dto.setUserId(transaction.getUser().getId());
        return dto;
    }
}