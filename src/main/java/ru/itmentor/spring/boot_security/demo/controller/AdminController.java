package ru.itmentor.spring.boot_security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.entity.User;
import ru.itmentor.spring.boot_security.demo.service.RoleServiceImp;
import ru.itmentor.spring.boot_security.demo.service.UserServiceImp;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImp userService;
    private final RoleServiceImp roleService;

    @GetMapping("/admin")
    public String showAllUser(Model model){
        List<User> users = userService.findAllUsers();
        model.addAttribute("allUser", users);
        return "admin";
    }

    @PostMapping("/admin/user-save")
    public String saveUser(User user){
        userService.getNotNullRole(user);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user-save")
    public String saveUserForm(Model model){
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", new User());
        return "admin-save";
    }

    @DeleteMapping("/admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user-update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "nameRoles", required = false) String[] roles){
        userService.getUserAndRoles(user, roles);
        userService.updateUser(user);
        return "redirect:/admin";
    }
    @GetMapping("/admin/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.findUserById(id));
        return "admin-update";
    }
}
