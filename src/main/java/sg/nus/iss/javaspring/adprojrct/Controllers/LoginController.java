package sg.nus.iss.javaspring.adprojrct.Controllers;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Services.UserService;

/*@Controller
public class LoginController {
    @Autowired
    private UserService userInterface;

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


        return "redirect:/login";
    }
}*/
@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user, HttpSession session, HttpServletResponse response) {
        if (validateUser(user.getUsername(), user.getPassword())) {
            User inuser = userService.findUserByUsername(user.getUsername());
            session.setAttribute("user", inuser);

            return ResponseEntity.ok(inuser); // 返回用户对象
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean validateUser(String username, String password) {
        User user = userService.authenticate(username, password);
        return user != null;
    }
}



