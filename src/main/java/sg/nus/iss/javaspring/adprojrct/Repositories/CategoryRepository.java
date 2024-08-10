package sg.nus.iss.javaspring.adprojrct.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sg.nus.iss.javaspring.adprojrct.Models.Category;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByUserId(int userId);
    Optional<Category> findByNameAndUserId(String name, Integer userId);
    List<Category> findByUserIdNot(int userId);
    List<Category> findByType(int type);
    @Query("SELECT c.name, COUNT(t) AS transactionCount FROM Category c " +
            "LEFT JOIN c.transactions t " +
            "GROUP BY c.id, c.name " +
            "ORDER BY transactionCount DESC")
    List<Object[]> findTopCategoriesWithMostTransactions(Pageable pageable);
}
