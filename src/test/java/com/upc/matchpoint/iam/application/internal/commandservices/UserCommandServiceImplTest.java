package com.upc.matchpoint.iam.application.internal.commandservices;

import com.upc.matchpoint.iam.application.internal.outboundservices.hashing.HashingService;
import com.upc.matchpoint.iam.application.internal.outboundservices.tokens.TokenService;
import com.upc.matchpoint.iam.domain.model.aggregates.User;
import com.upc.matchpoint.iam.domain.model.commands.SignInCommand;
import com.upc.matchpoint.iam.domain.model.commands.SignUpCommand;
import com.upc.matchpoint.iam.domain.model.entities.Role;
import com.upc.matchpoint.iam.domain.model.valueobjects.Roles;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HashingService hashingService;

    @Mock
    private TokenService tokenService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role(Roles.ROLE_USER);
        testUser = new User("testuser", "hashedPassword", List.of(testRole));
    }

    // --- sign-in tests ---

    @Test
    void signIn_withValidCredentials_returnsUserAndToken() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(hashingService.matches("rawPassword", "hashedPassword")).thenReturn(true);
        when(tokenService.generateToken("testuser")).thenReturn("jwt-token");

        Optional<ImmutablePair<User, String>> result = userCommandService.handle(new SignInCommand("testuser", "rawPassword"));

        assertThat(result).isPresent();
        assertThat(result.get().getLeft()).isEqualTo(testUser);
        assertThat(result.get().getRight()).isEqualTo("jwt-token");
    }

    @Test
    void signIn_withUnknownUsername_throwsRuntimeException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userCommandService.handle(new SignInCommand("unknown", "any")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void signIn_withWrongPassword_throwsRuntimeException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(hashingService.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        assertThatThrownBy(() -> userCommandService.handle(new SignInCommand("testuser", "wrongPassword")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid password");
    }

    // --- sign-up tests ---

    @Test
    void signUp_withNewUsername_createsAndReturnsUser() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_USER)).thenReturn(Optional.of(testRole));
        when(hashingService.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userCommandService.handle(new SignUpCommand("newuser", "password123", List.of()));

        assertThat(result).isPresent();
    }

    @Test
    void signUp_withExistingUsername_throwsRuntimeException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThatThrownBy(() -> userCommandService.handle(new SignUpCommand("testuser", "password123", List.of())))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Username already exists");
    }
}
