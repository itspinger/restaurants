package raf.rs.restaurants.userservice.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Client extends User {


    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "client", orphanRemoval = true)
    //private List<Reservation> reservations=new ArrayList<>();
    private Integer reservations_num = 0;
    public Client(Long id, String username, String email, String password, String firstName, String lastName, Date birthDate, Integer reservations_num) {
        super(id, username, email, password, firstName, lastName, birthDate);
        this.reservations_num = reservations_num;
    }

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }
}
