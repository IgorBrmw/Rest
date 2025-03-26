package com.example.IgorWebApp30.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class AdminController {

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }

//    private final UserService userService;
//    private final RoleService roleService;
//
//    @Autowired
//    public AdminController(UserService userService, RoleService roleService) {
//        this.userService = userService;
//        this.roleService = roleService;
//    }
//
//    @GetMapping
//    public String adminPanel(Model model, Principal principal) {
//        // Получаем текущего пользователя
//        User user = userService.getUserByUsername(principal.getName());
//
//        // Передаем данные в модель
//        model.addAttribute("user", user);
//        model.addAttribute("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")));
//        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("isAdminPanel", true);
//        model.addAttribute("isUserPanel", false);
//        // Передаем пустого пользователя для формы создания
//        model.addAttribute("newUser", new User());
//        // Передаем список всех ролей для выбора в форме
//        model.addAttribute("allRoles", roleService.getAllRoles());
//        return "index"; // Имя шаблона
//    }
//
//
//    @GetMapping("/user")
//    public String userPanel(Model model, Principal principal) {
//        User user = userService.getUserByUsername(principal.getName());
//        model.addAttribute("user", user);
//        model.addAttribute("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")));
//        model.addAttribute("isUserPanel", true);
//        return "index";
//    }
//
//    @GetMapping("/users")
//    public String listUsers(Model model) {
//        model.addAttribute("users", userService.getAllUsers());
//        return "index1";
//    }
//
//
//    @PostMapping("/users")
//    public String saveUser(@ModelAttribute("newUser") User user, @RequestParam("roleIds") List<Long> roleIds) {
//        Set<Role> roles = roleService.getRolesByIds(roleIds);
//        System.out.println("roles: " + roles);
//        user.setRoles(roles);
//        System.out.println("setroles");
//        userService.saveUser(user);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/users/update")
//    public String updateUser(@ModelAttribute("user") User user, @RequestParam("roleIds") List<Long> roleIds) {
//        Set<Role> roles = roleService.getRolesByIds(roleIds);
//        user.setRoles(roles);
//        userService.updateUser(user);
//        return "redirect:/admin";
//    }
//
//
//    @PostMapping("/users/{id}")
//    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user) {
//        userService.updateUser(user);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/users/delete/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        Useffinded = userService.getUserById(id);
//        if (finded.isEmpty()) {
//            throw new RuntimeException("User not found");
//        }
//        User user = finded.get();
//        user.getRoles().clear();
//        userService.saveUser(user);
//        userService.deleteUser(id);
//        return "redirect:/admin";
//    }

}
