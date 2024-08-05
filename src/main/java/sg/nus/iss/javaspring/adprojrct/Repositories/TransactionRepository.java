package sg.nus.iss.javaspring.adprojrct.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<List<Transaction>> findByCategoryId(int categoryId);

    Optional<List<Transaction>> findByUserId(int userId);

    @Query("SELECT l FROM Transaction l WHERE l.user.id = :userId ORDER BY l.created_at DESC")
    List<Transaction> findTransactionsByOrderDateAtDesc(@Param("userId") int userId);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.created_at BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsByUserIdAndDateRange(@Param("userId") int userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
