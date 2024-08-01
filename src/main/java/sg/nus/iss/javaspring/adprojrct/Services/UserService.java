package sg.nus.iss.javaspring.adprojrct.Services;

import sg.nus.iss.javaspring.adprojrct.Models.User;

public interface UserService {
    User saveUser(User user);
    User findUserByUsername(String username);
    User authenticate(String username, String password);
}
