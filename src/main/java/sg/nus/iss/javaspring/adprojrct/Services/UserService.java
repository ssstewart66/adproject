package sg.nus.iss.javaspring.adprojrct.Services;

import sg.nus.iss.javaspring.adprojrct.Models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> findUserByUsername(String username);
    User authenticate(String username, String password);
    List<User> findAllUsers();
    void deleteUserById(int userId);
    Optional<User> findUserById(int userId);
    User updateUser(User user,int userId);
}
