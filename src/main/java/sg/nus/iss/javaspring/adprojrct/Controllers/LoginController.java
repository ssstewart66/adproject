package sg.nus.iss.javaspring.adprojrct.Controllers;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Services.UserInterface;

@Controller
public class LoginController {
    @Autowired
    private UserInterface userInterface;

    @GetMapping(value = "/login")
    public String login(Model model, @CookieValue(value = "username", defaultValue = "") String username, @CookieValue(value = "password", defaultValue = "") String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping(value = "/login")
    public String Post(@ModelAttribute("user") User user, HttpSession session, HttpServletResponse response, Model model) {
        if(validateUser(user.getUsername(),user.getPassword())){
            User inuser = userInterface.findUserByUsername(user.getUsername());
            session.setAttribute("user", inuser);

            if (user.isRememberMe()) {
                Cookie usernameCookie = new Cookie("username", user.getUsername());
                Cookie passwordCookie = new Cookie("password", user.getPassword());
                usernameCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                passwordCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                response.addCookie(usernameCookie);
                response.addCookie(passwordCookie);
            } else {
                Cookie usernameCookie = new Cookie("username", null);
                Cookie passwordCookie = new Cookie("password", null);
                usernameCookie.setMaxAge(0);
                passwordCookie.setMaxAge(0);
                response.addCookie(usernameCookie);
                response.addCookie(passwordCookie);
            }
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
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();

        // Remove the cookies
        Cookie usernameCookie = new Cookie("username", null);
        Cookie passwordCookie = new Cookie("password", null);
        usernameCookie.setMaxAge(0);
        passwordCookie.setMaxAge(0);
        response.addCookie(usernameCookie);
        response.addCookie(passwordCookie);

        return "redirect:/login";
    }
}
