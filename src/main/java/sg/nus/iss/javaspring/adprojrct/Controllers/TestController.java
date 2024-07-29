package sg.nus.iss.javaspring.adprojrct.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping(value ="/policy")
    public String getPolicyPage() {
        return "policy";
    }
}