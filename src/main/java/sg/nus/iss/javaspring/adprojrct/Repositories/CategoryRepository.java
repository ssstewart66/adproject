package sg.nus.iss.javaspring.adprojrct.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.javaspring.adprojrct.Models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByUserId(int userId);
    Optional<Category> findByNameAndUserId(String name, Integer userId);
}
