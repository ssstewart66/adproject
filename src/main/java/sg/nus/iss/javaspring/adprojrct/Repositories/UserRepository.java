package sg.nus.iss.javaspring.adprojrct.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.javaspring.adprojrct.Models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
