package sg.nus.iss.javaspring.adprojrct.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/User")
public class UserController {
    @GetMapping(value = "/dashboard")
    public String dashboard(Model model) {
        return "userDashboard";
    }
}
