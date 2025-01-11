package raf.rs.reservations.security;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import raf.rs.reservations.security.service.TokenService;
import raf.rs.reservations.user.UserDetailsImpl;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public TokenAuthenticationFilter(TokenService jwtService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException, java.io.IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String username = this.jwtService.extractUsername(jwt);
            final Long userId = this.jwtService.get(jwt, "userId", Long.class);
            final Long restaurantId = this.jwtService.get(jwt, "restaurantId", Long.class);
            final Collection<? extends GrantedAuthority> authorities = this.jwtService.extractAuthorities(jwt)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                if (this.jwtService.isTokenValid(jwt, username)) {
                    final AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(

                        new UserDetailsImpl(username, userId, restaurantId, authorities),
                        null,
                        authorities
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            exception.printStackTrace();
            this.handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}