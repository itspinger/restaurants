package raf.rs.notification.user;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {
    private final String username;
    private final Long userId;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String username, Long userId, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.userId = userId;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public Long getUserId() {
        return this.userId;
    }

    public boolean hasRole(String role) {
        return this.authorities.contains(new SimpleGrantedAuthority(role));
    }

    public boolean isAdmin() {
        return this.hasRole("ROLE_ADMIN");
    }
}
