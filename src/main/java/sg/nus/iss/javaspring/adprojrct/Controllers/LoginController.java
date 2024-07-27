package sg.nus.iss.javaspring.adprojrct.Controllers;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Services.UserInterface;

@Controller
public class LoginController {
    @Autowired
    private UserInterface userInterface;

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping(value = "/login")
    public String Post(@ModelAttribute("user") User user, HttpSession session, Model model) {
        if(validateUser(user.getUsername(),user.getPassword())){
            User inuser = userInterface.findUserByUsername(user.getUsername());
            session.setAttribute("user", inuser);
            if(inuser.getRole()==0){
                return "redirect:/Admin/dashboard";
            }else if(inuser.getRole()==1){
                return "redirect:/User/dashboard";
            }else {
                model.addAttribute("error", "Unauthorized access");
                return "login";
            }
        }else{
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    private boolean validateUser(String username, String password) {
        User user = userInterface.authenticate(username, password);
        return user != null;
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
