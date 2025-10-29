package com.example.OnlineExaminationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.service.AuthService;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = authService.findByUsername(username).orElse(null);
        
        if (user != null) {
            model.addAttribute("user", user);
            switch (user.getRole()) {
                case "ADMIN":
                    return "redirect:/admin/dashboard";
                case "FACULTY":
                    return "redirect:/faculty/dashboard";
                case "STUDENT":
                    return "redirect:/student/dashboard";
                default:
                    return "login";
            }
        }
        return "login";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        try {
            authService.registerUser(user);
            model.addAttribute("message", "Registration successful! Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }
}
