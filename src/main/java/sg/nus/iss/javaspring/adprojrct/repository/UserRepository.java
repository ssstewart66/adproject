package sg.nus.iss.javaspring.adprojrct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.javaspring.adprojrct.Models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
