package raf.rs.notification.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    private final TokenAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationError customAuthenticationError;

    public SecurityConfig(TokenAuthenticationFilter jwtAuthenticationFilter, CustomAuthenticationError customAuthenticationError) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationError = customAuthenticationError;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .authorizeHttpRequests()
            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(this.customAuthenticationError)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless as no session is maintained
            .and()
            .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add filter for JWT validation

        return http.build();
    }
}
