package sg.nus.iss.javaspring.adprojrct.Controllers;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user, HttpSession session, HttpServletResponse response) {
        if (validateUser(user.getUsername(), user.getPassword())) {
            Optional<User> inuser = userService.findUserByUsername(user.getUsername());
            session.setAttribute("user", inuser);

            Cookie cookie = new Cookie("user_session", session.getId());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok(inuser.get());
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.invalidate();
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("user_session")) {
                cookie.setValue(null);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                break;
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean validateUser(String username, String password) {
        User user = userService.authenticate(username, password);
        return user != null;
    }
}




