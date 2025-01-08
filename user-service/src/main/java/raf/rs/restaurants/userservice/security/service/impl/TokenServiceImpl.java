package raf.rs.restaurants.userservice.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import raf.rs.restaurants.userservice.security.service.TokenService;

import java.security.Key;
import java.util.function.Function;

@Service
public class TokenServiceImpl implements TokenService {
    @Value("${security.jwt.secret-key}")
    private String jwtSecret;

    @Override
    public String generate(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, this.jwtSecret)
                .compact();
    }

    @Override
    public Claims parseToken(String jwt) {
        final Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.jwtSecret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
        return claims;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.parseToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String extractUsername(String jwt) {
        return this.extractClaim(jwt, (claims) -> claims.get("username", String.class));
    }

    @Override
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = this.extractUsername(jwt);
        return (username.equals(userDetails.getUsername()));
    }
}
