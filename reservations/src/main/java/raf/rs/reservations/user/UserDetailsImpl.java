package raf.rs.reservations.user;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {
    private final String username;
    private final Long userId;
    private final Long restaurantId;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String username, Long userId, Long restaurantId, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.userId = userId;
        this.restaurantId = restaurantId;
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

    public Long getRestaurantId() {
        return this.restaurantId;
    }

    public boolean hasRole(String role) {
        return this.authorities.contains(new SimpleGrantedAuthority(role));
    }

    public boolean isManager() {
        return this.restaurantId != null && this.hasRole("ROLE_MANAGER");
    }
}
