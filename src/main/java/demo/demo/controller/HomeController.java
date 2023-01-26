package demo.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("")
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

}