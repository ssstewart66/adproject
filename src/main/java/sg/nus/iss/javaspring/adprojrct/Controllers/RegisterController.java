package sg.nus.iss.javaspring.adprojrct.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.services.UserInterface;

import java.time.LocalDate;

@Controller
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
}

