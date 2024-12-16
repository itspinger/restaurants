package raf.rs.restaurants.userservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    private String password;
    private String firstName;
    private String lastName;
    private Date birthDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of();
    }

}
