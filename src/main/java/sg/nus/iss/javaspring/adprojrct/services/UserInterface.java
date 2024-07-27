package sg.nus.iss.javaspring.adprojrct.services;

import sg.nus.iss.javaspring.adprojrct.Models.User;

public interface UserInterface {
    User saveUser(User user);
    User findUserByUsername(String username);
    User authenticate(String username, String password);
}
