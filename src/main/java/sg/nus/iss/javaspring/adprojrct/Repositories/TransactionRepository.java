package sg.nus.iss.javaspring.adprojrct.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
