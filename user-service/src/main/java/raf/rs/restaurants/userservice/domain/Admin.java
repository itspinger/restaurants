package raf.rs.restaurants.userservice.domain;

import jakarta.persistence.Entity;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

@Entity
public class Admin extends User {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
