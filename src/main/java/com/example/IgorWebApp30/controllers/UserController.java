package com.example.IgorWebApp30.controllers;

import com.example.IgorWebApp30.model.Role;
import com.example.IgorWebApp30.model.User;
import com.example.IgorWebApp30.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String userPanel(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")));
        model.addAttribute("isAdminPanel", false);
        model.addAttribute("isUserPanel", true);

        return "admin";
    }
}