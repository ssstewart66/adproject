package sg.nus.iss.javaspring.adprojrct.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<List<Transaction>> findByCategoryId(int categoryId);

    Optional<List<Transaction>> findByUserId(int userId);

}
