package com.upc.matchpoint.coaches.interfaces.rest.transform;

import com.upc.matchpoint.coaches.domain.model.commands.CreateCoachCommand;
import com.upc.matchpoint.coaches.interfaces.rest.resources.CreateCoachResource;

public class CreateCoachCommandFromResourceAssembler {
    public static CreateCoachCommand toCommandFromResource(CreateCoachResource resource) {
        return new CreateCoachCommand(
                resource.name(),
                resource.expertise(),
                resource.phone()
        );
    }
}

