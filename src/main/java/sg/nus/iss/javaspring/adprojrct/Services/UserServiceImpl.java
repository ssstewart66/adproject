package sg.nus.iss.javaspring.adprojrct.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return user;
        }else{
            return null;
        }
    }

    @Override
    public User authenticate(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> findUserById(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User updateUser(User newUser, int userId) {
        return userRepository.findById(userId).map(user -> {
            if (!user.getUsername().equals(newUser.getUsername())) {
                // 检查是否存在相同用户名的用户
                Optional<User> existingUser = userRepository.findByUsername(newUser.getUsername());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                    throw new IllegalArgumentException("User with the same username already exists");
                }
            }
            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());
            user.setEmail(newUser.getEmail());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
