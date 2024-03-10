package application.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import application.models.AuditLog;
import application.models.Role;
import application.models.User;
import application.repos.AuditLogRepo;
import application.repos.RoleRepo;
import application.repos.UserRepo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AddRoleController {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final AuditLogRepo auditLogRepo;

    @PostMapping("/addrole/admin")
    public String addAdminRole(@RequestParam String username) {
        return addRoleToUser(username, "ROLE_ADMIN");
    }

    @PostMapping("/addrole/user")
    public String addUserRole(@RequestParam String username) {
        return addRoleToUser(username, "ROLE_USERS");
    }

    @PostMapping("/addrole/posts")
    public String addAlbumRole(@RequestParam String username) {
        return addRoleToUser(username, "ROLE_POSTS");
    }

    @PostMapping("/addrole/albums")
    public String registerPostUser(@RequestParam String username) {
        return addRoleToUser(username, "ROLE_ALBUMS");
    }

    private String addRoleToUser(String username, String roleName) {
        if (!userRepository.existsByUsername(username)) {
            return "Username doesn't exists!";
        }
        User user = userRepository.getUserByUsername(username);
        Role role = roleRepository.getRoleByName(roleName);
        if (role == null) {
            return "Role doesn't exists";
        }
        if (user.getRoles().contains(role)) {
            return "User already has role " + roleName;
        }
        user.addRole(role);
        userRepository.save(user);
        auditLogRepo.save(new AuditLog("Role " + roleName + " was successfully added to user ", username));
        return "Role " + roleName + " was successfully added to user " + username;
    }
}
