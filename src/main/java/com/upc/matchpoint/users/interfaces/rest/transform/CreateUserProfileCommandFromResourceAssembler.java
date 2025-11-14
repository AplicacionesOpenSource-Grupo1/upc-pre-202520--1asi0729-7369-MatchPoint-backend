package com.upc.matchpoint.users.interfaces.rest.transform;

import com.upc.matchpoint.users.domain.model.commands.CreateUserProfileCommand;
import com.upc.matchpoint.users.interfaces.rest.resources.CreateUserProfileResource;

public class CreateUserProfileCommandFromResourceAssembler {
    public static CreateUserProfileCommand toCommandFromResource(CreateUserProfileResource resource) {
        return new CreateUserProfileCommand(
                resource.name(),
                resource.email(),
                resource.phone()
        );
    }
}

