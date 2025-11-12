package com.upc.matchpoint.users.interfaces.rest;

import com.upc.matchpoint.users.domain.model.commands.DeleteUserProfileCommand;
import com.upc.matchpoint.users.domain.model.queries.GetAllUserProfilesQuery;
import com.upc.matchpoint.users.domain.model.queries.GetUserProfileByIdQuery;
import com.upc.matchpoint.users.domain.services.UserProfileCommandService;
import com.upc.matchpoint.users.domain.services.UserProfileQueryService;
import com.upc.matchpoint.users.interfaces.rest.resources.CreateUserProfileResource;
import com.upc.matchpoint.users.interfaces.rest.resources.UpdateUserProfileResource;
import com.upc.matchpoint.users.interfaces.rest.resources.UserProfileResource;
import com.upc.matchpoint.users.interfaces.rest.transform.CreateUserProfileCommandFromResourceAssembler;
import com.upc.matchpoint.users.interfaces.rest.transform.UpdateUserProfileCommandFromResourceAssembler;
import com.upc.matchpoint.users.interfaces.rest.transform.UserProfileResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Profiles", description = "User Profile Management Endpoints")
public class UserProfilesController {
    private final UserProfileCommandService userProfileCommandService;
    private final UserProfileQueryService userProfileQueryService;

    public UserProfilesController(UserProfileCommandService userProfileCommandService, UserProfileQueryService userProfileQueryService) {
        this.userProfileCommandService = userProfileCommandService;
        this.userProfileQueryService = userProfileQueryService;
    }

    @PostMapping
    public ResponseEntity<UserProfileResource> createUserProfile(@RequestBody CreateUserProfileResource resource) {
        var command = CreateUserProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var userProfile = userProfileCommandService.handle(command);
        return userProfile.map(profile -> new ResponseEntity<>(UserProfileResourceFromEntityAssembler.toResourceFromEntity(profile), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResource>> getAllUserProfiles() {
        var query = new GetAllUserProfilesQuery();
        var userProfiles = userProfileQueryService.handle(query);
        var userProfileResources = userProfiles.stream()
                .map(UserProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(userProfileResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResource> getUserProfileById(@PathVariable Long id) {
        var query = new GetUserProfileByIdQuery(id);
        var userProfile = userProfileQueryService.handle(query);
        return userProfile.map(profile -> ResponseEntity.ok(UserProfileResourceFromEntityAssembler.toResourceFromEntity(profile)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResource> updateUserProfile(@PathVariable Long id, @RequestBody UpdateUserProfileResource resource) {
        var command = UpdateUserProfileCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedUserProfile = userProfileCommandService.handle(command);
        return updatedUserProfile.map(profile -> ResponseEntity.ok(UserProfileResourceFromEntityAssembler.toResourceFromEntity(profile)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserProfile(@PathVariable Long id) {
        var command = new DeleteUserProfileCommand(id);
        userProfileCommandService.handle(command);
        return ResponseEntity.ok("User deleted successfully.");
    }
}

