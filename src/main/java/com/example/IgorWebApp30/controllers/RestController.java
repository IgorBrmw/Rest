package com.example.IgorWebApp30.controllers;

import com.example.IgorWebApp30.exception_handling.DataInfoHandler;
import com.example.IgorWebApp30.exception_handling.NoSuchUserException;
import com.example.IgorWebApp30.exception_handling.UserWithSuchLoginExist;
import com.example.IgorWebApp30.model.Role;
import com.example.IgorWebApp30.model.User;
import com.example.IgorWebApp30.service.RoleService;
import com.example.IgorWebApp30.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            System.out.println("User with id " + id + " not found");
            throw new NoSuchUserException("There is no user with id=" + id
                    + " in database");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.saveUser(user);
        return  ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
            userService.updateUser(user);
            return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            System.out.println("User with id " + id + " not found");
            throw new NoSuchUserException("There is no user with id=" + id
                    + " in database");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().body(user);

    }

    @GetMapping("/roles")

    public ResponseEntity<Iterable<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Формируем ответ в виде Map
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        List<Map<String, Object>> roles = user.getRoles().stream()
                .map(role -> {
                    Map<String, Object> roleMap = new HashMap<>();
                    roleMap.put("id", role.getId());
                    roleMap.put("name", role.getName());
                    return roleMap;
                })
                .collect(Collectors.toList());

        response.put("roles", roles);

        return ResponseEntity.ok(response);
    }


}
