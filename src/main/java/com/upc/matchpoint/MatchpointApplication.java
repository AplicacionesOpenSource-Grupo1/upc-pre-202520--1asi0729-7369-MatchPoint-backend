package com.upc.matchpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.upc.matchpoint.iam.domain.model.valueobjects.Roles;
import com.upc.matchpoint.iam.domain.model.entities.Role;
import com.upc.matchpoint.iam.domain.model.aggregates.User;
import com.upc.matchpoint.iam.infrastructure.hashing.bcrypt.BCryptHashingService;

@SpringBootApplication
@EnableJpaAuditing
public class MatchpointApplication {
    //Hello world
    public static void main(String[] args) {
        SpringApplication.run(MatchpointApplication.class, args);
    }

    @Bean
    public CommandLineRunner createMasterUser(UserRepository userRepository, RoleRepository roleRepository, BCryptHashingService hashingService) {
        return args -> {
            String masterEmail = "juancarlosanguloabud@gmail.com";
            String masterPassword = "juancarlosanguloabud";
            if (!userRepository.existsByUsername(masterEmail)) {
                Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN)
                        .orElseGet(() -> roleRepository.save(new Role(Roles.ROLE_ADMIN)));
                User master = new User(masterEmail, hashingService.encode(masterPassword));
                master.addRole(adminRole);
                userRepository.save(master);
                System.out.println("Usuario maestro creado: " + masterEmail);
            } else {
                System.out.println("Usuario maestro ya existe: " + masterEmail);
            }
        };
    }

}
