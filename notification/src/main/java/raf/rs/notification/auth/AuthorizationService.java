package raf.rs.notification.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import raf.rs.notification.exception.NotYoursException;
import raf.rs.notification.user.UserDetailsImpl;

@Service
public class AuthorizationService {

    public boolean canViewNotifications(Authentication authentication, Long userId) {
        final UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        if (user.getUserId().equals(userId)) {
            return true;
        }

        throw new NotYoursException("You may not edit this profile");
    }

}
