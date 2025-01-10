package raf.rs.restaurants.userservice.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import raf.rs.restaurants.userservice.domain.User;
import raf.rs.restaurants.userservice.exception.NotYoursException;

@Service
public class AuthorizationService {

    public boolean canEditProfile(Authentication authentication, Long userId) {
        final User user = (User) authentication.getPrincipal();
        if (user.getId().equals(userId)) {
            return true;
        }

        throw new NotYoursException("You may not edit this profile");
    }

}
