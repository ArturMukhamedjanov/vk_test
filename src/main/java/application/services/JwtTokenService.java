package application.services;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import application.models.User;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class JwtTokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Set<String> getRoles(String token){
        Set<String> roles = new HashSet<>();
        if(validateToken(token)){
            try {
                Claims claims = Jwts.parser()
                                   .setSigningKey(secretKey)
                                   .parseClaimsJws(token)
                                   .getBody();
                roles = new HashSet<>(claims.get("roles", Set.class));
            } catch (Exception e) {
                // Handle any potential exceptions here
                e.printStackTrace();
            }
        }
        return roles;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
