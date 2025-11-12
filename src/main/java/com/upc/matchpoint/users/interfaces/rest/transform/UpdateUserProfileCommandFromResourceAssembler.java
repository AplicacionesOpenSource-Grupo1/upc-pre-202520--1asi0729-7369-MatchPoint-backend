package com.upc.matchpoint.users.interfaces.rest.transform;

import com.upc.matchpoint.users.domain.model.commands.UpdateUserProfileCommand;
import com.upc.matchpoint.users.interfaces.rest.resources.UpdateUserProfileResource;

public class UpdateUserProfileCommandFromResourceAssembler {
    public static UpdateUserProfileCommand toCommandFromResource(Long userId, UpdateUserProfileResource resource) {
        return new UpdateUserProfileCommand(
                userId,
                resource.name(),
                resource.email(),
                resource.phone()
        );
    }
}

