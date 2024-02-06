package ru.itmentor.spring.boot_security.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.entity.Role;
import ru.itmentor.spring.boot_security.demo.entity.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Override
    public User findUserById(Long id) {
//        return userRepository.findById(id).orElse(null);
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElse(new User());
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll().stream().sorted(Comparator.comparing(User::getId)).toList();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
//        return userRepository.findUserByUsername(username);
        UserDetails userDetails = userRepository.findUserByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User with this " + username + " User Name not found");
        }
        return userDetails;
    }

    @Override
    public boolean saveUser(User user) {
        System.out.println(user);
        User userFromBD = userRepository.findUserByUsername(user.getUsername());
        if (userFromBD !=null)
            return false;
//        if (user.getUsername().isEmpty() | user.getPassword().isEmpty())
//            return false;
        if (user.getUsername().equals("") | user.getPassword().equals(""))
            return false;
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        if (user.getUsername().equals("")) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public Long getUsernameByName(String name) {
        return userRepository.findUserByUsername(name).getId();
    }

    @Override
    public User getUserAndRoles(User user, String[] roles) {
        user.setRoles(roleService.getRoleByName(Objects.requireNonNullElseGet(roles, () -> new String[]{"ROLE_USER"})));
        return user;

//        if (roles == null) {
//            user.setRoles(roleService.getRoleByName(new String[]{"ROLE_USER"}));
//        } else {
//            user.setRoles(roleService.getRoleByName(roles));
//        }
//        return user;
    }

    @Override
    public User getNotNullRole(User user) {
        if (user.getRoles() == null) {
            user.setRoles(Collections.singleton(new Role(2L)));
        }
        return user;
    }

    public boolean saveUserTest(User user) {
        User userFromDB = userRepository.findUserByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
    public User getUserForUpdateUsers(User user, String username) {
        User userDb = findUserById(getUsernameByName(username));
        Set<Role> roles = userDb.getRoles();
        user.setRoles(roleService.getRoleByName(roles.stream().map(role -> role.getName()).toArray(String[]::new)));
        return user;
    }

    @PostConstruct
    public void addTestUsers() {
        roleRepository.save(new Role(1L, "ROLE_ADMIN"));
        roleRepository.save(new Role(2L, "ROLE_USER"));
        User newAdmin = new User("admin"
                , "admin"
                , roleService.getRoleByName(new String[]{"ROLE_ADMIN"})
                , null
                , 0);
        saveUserTest(newAdmin);
        User newUser = new User("user"
                , "user"
                , roleService.getRoleByName(new String[]{"ROLE_USER"})
                , null,
                0);
        saveUserTest(newUser);
    }
}
