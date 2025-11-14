package com.upc.matchpoint.courts.interfaces.rest.transform;

import com.upc.matchpoint.courts.domain.model.commands.UpdateCourtCommand;
import com.upc.matchpoint.courts.interfaces.rest.resources.UpdateCourtResource;

public class UpdateCourtCommandFromResourceAssembler {
    public static UpdateCourtCommand toCommandFromResource(Long courtId, UpdateCourtResource resource) {
        return new UpdateCourtCommand(
                courtId,
                resource.name(),
                resource.location(),
                resource.type()
        );
    }
}
