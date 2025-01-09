package raf.rs.reservations.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationError implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        System.out.println(authException.getMessage());

        if (authException instanceof DisabledException) {
            response.getWriter().write("{\"error\": \"Access denied\", \"message\": \"Your account is disabled. Please contact the administrator.\"}");
        } else {
            response.getWriter().write("{\"error\": \"Access denied\", \"message\": \"You are not authorized to access this resource.\"}");
        }
    }
}
