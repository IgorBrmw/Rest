package com.example.IgorWebApp30.controllers;

import com.example.IgorWebApp30.model.Role;
import com.example.IgorWebApp30.model.User;
import com.example.IgorWebApp30.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//
//@Controller
//public class UserController {
//
//    UserService userService;
//
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/user")
//    public String userPanel(Model model, Principal principal) {
//        User user = userService.getUserByUsername(principal.getName());
//
//        model.addAttribute("user", user);
//        model.addAttribute("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")));
//        model.addAttribute("isAdminPanel", false);
//        model.addAttribute("isUserPanel", true);
//
//        return "Index1";
//    }
//}
//@GetMapping("/current-user")
//public ResponseEntity<Map<String, Object>> getCurrentUser(Principal principal) {
//    User user = userService.getUserByUsername(principal.getName());
//
//    if (user == null) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//
//    // Формируем ответ в виде Map
//    Map<String, Object> response = new HashMap<>();
//    response.put("id", user.getId());
//    response.put("username", user.getUsername());
//    response.put("email", user.getEmail());
//
//    // Преобразуем роли в удобный для фронтенда формат
//    List<Map<String, Object>> roles = user.getRoles().stream()
//            .map(role -> {
//                Map<String, Object> roleMap = new HashMap<>();
//                roleMap.put("id", role.getId());
//                roleMap.put("name", role.getName());
//                return roleMap;
//            })
//            .collect(Collectors.toList());
//
//    response.put("roles", roles);
//
//    return ResponseEntity.ok(response);
//}
