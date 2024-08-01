package sg.nus.iss.javaspring.adprojrct.Services;

import sg.nus.iss.javaspring.adprojrct.Models.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findUserByUsername(String username);
    User authenticate(String username, String password);
    List<User> findAllUsers();
    void deleteUserById(int userId);
}
