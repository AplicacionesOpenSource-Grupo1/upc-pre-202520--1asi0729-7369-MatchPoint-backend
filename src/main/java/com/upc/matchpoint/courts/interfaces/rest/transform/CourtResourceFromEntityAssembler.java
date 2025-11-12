package com.upc.matchpoint.courts.interfaces.rest.transform;

import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.interfaces.rest.resources.CourtResource;

public class CourtResourceFromEntityAssembler {
    public static CourtResource toResourceFromEntity(Court entity) {
        return new CourtResource(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getType()
        );
    }
}

