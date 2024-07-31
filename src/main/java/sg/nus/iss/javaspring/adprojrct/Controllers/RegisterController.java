package sg.nus.iss.javaspring.adprojrct.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Services.UserInterface;

import java.time.LocalDate;

/*@Controller
public class RegisterController {
    @Autowired
    private UserInterface userInterface;

    @GetMapping(value = "/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value = "/register")
    public String register(@ModelAttribute User user, Model model) {
        String username = user.getUsername();
        if(userInterface.findUserByUsername(username) == null){
            user.setRole(1);
            user.setCreated_at(LocalDate.now());
            userInterface.saveUser(user);
            return "redirect:/login";
        } else {
            model.addAttribute("message", "Username already in use");
            return "register";
        }
    }
}*/

@RestController
@RequestMapping("/api")
public class RegisterController {
    @Autowired
    private UserInterface userInterface;

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String username = user.getUsername();
        if (userInterface.findUserByUsername(username) == null) {
            user.setRole(1);
            user.setCreated_at(LocalDate.now());
            userInterface.saveUser(user);
            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already in use");
        }
    }
}

