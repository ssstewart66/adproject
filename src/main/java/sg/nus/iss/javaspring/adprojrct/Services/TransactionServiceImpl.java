package sg.nus.iss.javaspring.adprojrct.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;
import sg.nus.iss.javaspring.adprojrct.Repositories.TransactionRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;

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
}
