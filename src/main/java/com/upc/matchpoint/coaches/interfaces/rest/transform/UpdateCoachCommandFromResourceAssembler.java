package com.upc.matchpoint.coaches.interfaces.rest.transform;

import com.upc.matchpoint.coaches.domain.model.commands.UpdateCoachCommand;
import com.upc.matchpoint.coaches.interfaces.rest.resources.UpdateCoachResource;

public class UpdateCoachCommandFromResourceAssembler {
    public static UpdateCoachCommand toCommandFromResource(Long coachId, UpdateCoachResource resource) {
        return new UpdateCoachCommand(
                coachId,
                resource.name(),
                resource.expertise(),
                resource.phone()
        );
    }
}

