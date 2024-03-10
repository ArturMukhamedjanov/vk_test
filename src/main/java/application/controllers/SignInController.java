package application.controllers;

import application.models.AuditLog;
import application.models.JwtRequest;
import application.models.JwtResponse;
import application.repos.AuditLogRepo;
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
    private final AuditLogRepo auditLogRepo;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        JwtResponse token = new JwtResponse(null, null);
        try {
            token = authService.login(authRequest);
        } catch (Exception e) {
            auditLogRepo.save(new AuditLog("User failed sign in", authRequest.getUsername()));
            return ResponseEntity.status(400).body("Wrong sign in data");
        }
        auditLogRepo.save(new AuditLog("User successfully signed in", authRequest.getUsername()));
        return ResponseEntity.ok(token);
    }
}
