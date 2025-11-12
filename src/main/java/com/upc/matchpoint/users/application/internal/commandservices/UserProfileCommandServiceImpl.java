package com.upc.matchpoint.users.application.internal.commandservices;

import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.domain.model.commands.CreateUserProfileCommand;
import com.upc.matchpoint.users.domain.model.commands.DeleteUserProfileCommand;
import com.upc.matchpoint.users.domain.model.commands.UpdateUserProfileCommand;
import com.upc.matchpoint.users.domain.services.UserProfileCommandService;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserProfileCommandServiceImpl implements UserProfileCommandService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileCommandServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<UserProfile> handle(CreateUserProfileCommand command) {
        if (userProfileRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("User with email " + command.email() + " already exists");
        }
        var userProfile = new UserProfile(command.name(), command.email(), command.phone());
        var createdUserProfile = userProfileRepository.save(userProfile);
        return Optional.of(createdUserProfile);
    }

    @Override
    public Optional<UserProfile> handle(UpdateUserProfileCommand command) {
        return userProfileRepository.findById(command.userId()).map(userProfileToUpdate -> {
            userProfileToUpdate.updateProfile(command.name(), command.email(), command.phone());
            return userProfileRepository.save(userProfileToUpdate);
        });
    }

    @Override
    public void handle(DeleteUserProfileCommand command) {
        if (!userProfileRepository.existsById(command.userId())) {
            throw new IllegalArgumentException("User with id " + command.userId() + " not found");
        }
        userProfileRepository.deleteById(command.userId());
    }
}

