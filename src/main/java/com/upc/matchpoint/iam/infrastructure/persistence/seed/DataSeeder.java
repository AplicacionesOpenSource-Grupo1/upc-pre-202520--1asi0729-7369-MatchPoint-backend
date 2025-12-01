package com.upc.matchpoint.iam.infrastructure.persistence.seed;

import com.upc.matchpoint.iam.domain.model.entities.Role;
import com.upc.matchpoint.iam.domain.model.valueobjects.Roles;
import com.upc.matchpoint.iam.domain.model.aggregates.User;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.upc.matchpoint.iam.infrastructure.hashing.bcrypt.BCryptHashingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Data seeder that inserts default roles and an admin user if they do not exist.
 * This runs on application startup.
 */
@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptHashingService hashingService;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, BCryptHashingService hashingService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.hashingService = hashingService;
    }

    @Override
    public void run(String... args) {
        // Ensure roles exist
        ensureRole(Roles.ROLE_USER);
        ensureRole(Roles.ROLE_ADMIN);
        ensureRole(Roles.ROLE_INSTRUCTOR);

        // Ensure admin user exists
        if (!userRepository.existsByUsername("admin")) {
            var adminRole = roleRepository.findByName(Roles.ROLE_ADMIN).orElseGet(() -> roleRepository.save(new Role(Roles.ROLE_ADMIN)));
            var encoded = hashingService.encode("admin");
            var admin = new User("admin", encoded, List.of(adminRole));
            userRepository.save(admin);
        }
    }

    private void ensureRole(Roles role) {
        if (!roleRepository.existsByName(role)) {
            roleRepository.save(new Role(role));
        }
    }
}
