package application.controllers;

import application.models.JwtRequest;
import application.models.JwtResponse;
import application.services.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignInController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        JwtResponse token = new JwtResponse(null, null);
        try {
            token = authService.login(authRequest);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Wrong sign in data");
        }
        
        return ResponseEntity.ok(token);
    }
}
