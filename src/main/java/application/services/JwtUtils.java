package application.services;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import application.models.JwtAuthentication;
import application.repos.UserRepo;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class JwtUtils {

    private final UserRepo userRepo;

    public JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(userRepo.getUserByUsername(claims.getSubject()).getRoles());
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

}