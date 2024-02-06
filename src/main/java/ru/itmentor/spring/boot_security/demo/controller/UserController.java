package ru.itmentor.spring.boot_security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmentor.spring.boot_security.demo.entity.User;
import ru.itmentor.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImp userService;

    @GetMapping("/user")
    public String showUser(Principal principal, Model model) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("oneUser", user);
        return "/user";

    }

    @GetMapping("/user/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "user-update";
    }

    @PostMapping("/user/user-update")
    public String updateUsers(@ModelAttribute("user") User user, Principal principal) {
        userService.getUserForUpdateUsers(user, principal.getName());
        userService.updateUser(user);
        return "redirect:/user";
    }

    @GetMapping("/news")
    public String showNews() {
        return "/news";
    }
}
