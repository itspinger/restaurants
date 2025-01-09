package raf.rs.restaurants.userservice.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.rs.restaurants.userservice.domain.Admin;
import raf.rs.restaurants.userservice.repository.UserRepository;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public TestDataRunner(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (this.userRepository.existsByUsername("admin")) {
            return;
        }

        final Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(this.encoder.encode("admin"));
        this.userRepository.save(admin);
    }
}
