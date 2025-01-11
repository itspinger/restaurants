package raf.rs.restaurants.userservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Inheritance
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String username;

    @Column(unique=true)
    private String email;

    @ColumnDefault("false")
    private boolean activated;

    @ColumnDefault("false")
    private boolean banned;

    private String password;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String verificationToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public List<String> getRoles() {
        return this.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    @Override
    public boolean isEnabled() {
        return !this.banned;
    }
}
