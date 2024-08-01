package sg.nus.iss.javaspring.adprojrct.Services;

import sg.nus.iss.javaspring.adprojrct.DTO.TransactionDTO;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<TransactionDTO> getAllTransactions();
    Optional<List<TransactionDTO>> getTransactionsByCategoryId(int categoryId);
    Optional<TransactionDTO> getTransactionById(int transactionId);
    List<TransactionDTO> findTransactionsByUserId(int userId);
    TransactionDTO addTransaction(TransactionDTO transaction, int userId);
    TransactionDTO updateTransaction(TransactionDTO transaction, int transactionId);
    void deleteTransaction(int transactionId);
}