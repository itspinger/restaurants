package raf.rs.reservations.security.service;

import io.jsonwebtoken.Claims;
import java.util.Collection;
import java.util.function.Function;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    String generate(Claims claims);

    Claims parseToken(String jwt);

    String extractUsername(String jwt);

    Collection<? extends GrantedAuthority> extractAuthorities(String jwt);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    <T> T get(String token, String key, Class<T> classifier);

    boolean isTokenValid(String jwt, UserDetails userDetails);

    boolean isTokenValid(String jwt, String username);
}
