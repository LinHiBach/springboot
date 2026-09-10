package vn.itstar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String login() {
        return "web/login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin")
    public String adminHome() {
        return "redirect:/admin/categories";
    }
}
