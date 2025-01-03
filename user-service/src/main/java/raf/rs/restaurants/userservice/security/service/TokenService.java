package raf.rs.restaurants.userservice.security.service;

import io.jsonwebtoken.Claims;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    String generate(Claims claims);

    Claims parseToken(String jwt);

    String extractUsername(String jwt);

    boolean isTokenValid(String jwt, UserDetails userDetails);
}
