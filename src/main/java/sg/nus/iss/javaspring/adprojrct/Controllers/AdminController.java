package sg.nus.iss.javaspring.adprojrct.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/Admin")
public class AdminController {

    @GetMapping(value = "/dashboard")
    public String dashboard(Model model) {
        return "adminDashboard";
    }
}
