package com.upc.matchpoint.iam.interfaces.rest;

import com.upc.matchpoint.iam.domain.model.aggregates.User;
import com.upc.matchpoint.iam.domain.model.entities.Role;
import com.upc.matchpoint.iam.domain.model.valueobjects.Roles;
import com.upc.matchpoint.iam.domain.services.UserCommandService;
import com.upc.matchpoint.iam.interfaces.rest.resources.SignInResource;
import com.upc.matchpoint.iam.interfaces.rest.resources.SignUpResource;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import com.upc.matchpoint.iam.domain.model.commands.SignInCommand;
import com.upc.matchpoint.iam.domain.model.commands.SignUpCommand;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserCommandService userCommandService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private User buildUser(String username) {
        Role role = new Role(Roles.ROLE_USER);
        return new User(username, "hashedPassword", List.of(role));
    }

    // --- sign-in tests ---

    @Test
    void signIn_withValidCredentials_returns200() {
        User user = buildUser("testuser");
        when(userCommandService.handle(any(SignInCommand.class))).thenReturn(Optional.of(ImmutablePair.of(user, "jwt-token")));

        ResponseEntity<?> response = authenticationController.signIn(new SignInResource("testuser", "password"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void signIn_withInvalidCredentials_returns401() {
        when(userCommandService.handle(any(SignInCommand.class))).thenThrow(new RuntimeException("Invalid password"));

        ResponseEntity<?> response = authenticationController.signIn(new SignInResource("testuser", "wrongpassword"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void signIn_withUnknownUser_returns401() {
        when(userCommandService.handle(any(SignInCommand.class))).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<?> response = authenticationController.signIn(new SignInResource("unknown", "anypassword"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // --- sign-up tests ---

    @Test
    void signUp_withNewCredentials_returns201() {
        User user = buildUser("newuser");
        when(userCommandService.handle(any(SignUpCommand.class))).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authenticationController.signUp(new SignUpResource("newuser", "password123", List.of()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void signUp_withExistingUsername_returns400() {
        when(userCommandService.handle(any(SignUpCommand.class))).thenThrow(new RuntimeException("Username already exists"));

        ResponseEntity<?> response = authenticationController.signUp(new SignUpResource("existing", "password123", List.of()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
