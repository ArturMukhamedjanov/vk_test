package application.controllers;

import application.models.User;
import application.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignInController {

    private final UserRepo userRepository;

    @Autowired
    public SignInController(UserRepo userRepo){
        userRepository = userRepo;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = null;
        try{
            user = loadUserByUsername(username);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        return ResponseEntity.ok("Login successful!");
    }

    private User loadUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        
        if (user == null) {
            throw new RuntimeException();
        }
        return user;
    }
}
