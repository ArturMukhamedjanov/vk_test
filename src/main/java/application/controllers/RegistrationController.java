package application.controllers;

import application.models.Role;
import application.models.User;
import application.repos.RoleRepo;
import application.repos.UserRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register/admin")
    public String registerAdmin(@RequestParam String username, @RequestParam String password) {
        return registerUser(username, password, "ROLE_ADMIN");
    }

    @PostMapping("/register/user")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        return registerUser(username, password, "ROLE_USERS");
    }

    @PostMapping("/register/albums")
    public String registerAlbumUser(@RequestParam String username, @RequestParam String password) {
        return registerUser(username, password, "ROLE_ALBUMS");
    }

    @PostMapping("/register/posts")
    public String registerPostUser(@RequestParam String username, @RequestParam String password) {
        return registerUser(username, password, "ROLE_POSTS");
    }

    private String registerUser(String username, String password, String roleName) {
        if (userRepository.existsByUsername(username)) {
            return "Username already exists!";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role role = roleRepository.getRoleByName(roleName);
        System.out.println(role.getName());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully!";
    }
}