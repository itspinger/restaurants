package raf.rs.reservations.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collection;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import raf.rs.reservations.security.service.TokenService;

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

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.parseToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public <T> T get(String token, String key, Class<T> classifier) {
        return this.extractClaim(token, (claims) -> claims.get(key, classifier));
    }

    @Override
    public String extractUsername(String jwt) {
        return this.extractClaim(jwt, (claims) -> claims.get("username", String.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends String> extractAuthorities(String jwt) {
        return (Collection<? extends String>) this.extractClaim(jwt, (claims) -> claims.get("roles", Collection.class));
    }

    @Override
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        return this.isTokenValid(jwt, userDetails.getUsername());
    }

    @Override
    public boolean isTokenValid(String jwt, String username) {
        return this.extractUsername(jwt).equals(username);
    }
}
