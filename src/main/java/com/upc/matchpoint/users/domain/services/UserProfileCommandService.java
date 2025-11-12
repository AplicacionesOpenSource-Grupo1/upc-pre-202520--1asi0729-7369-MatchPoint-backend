package com.upc.matchpoint.users.domain.services;

import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.domain.model.commands.CreateUserProfileCommand;
import com.upc.matchpoint.users.domain.model.commands.DeleteUserProfileCommand;
import com.upc.matchpoint.users.domain.model.commands.UpdateUserProfileCommand;
import java.util.Optional;

public interface UserProfileCommandService {
    Optional<UserProfile> handle(CreateUserProfileCommand command);
    Optional<UserProfile> handle(UpdateUserProfileCommand command);
    void handle(DeleteUserProfileCommand command);
}

