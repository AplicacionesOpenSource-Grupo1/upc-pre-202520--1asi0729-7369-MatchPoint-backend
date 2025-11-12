package com.upc.matchpoint.users.interfaces.rest.transform;

import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.interfaces.rest.resources.UserProfileResource;

public class UserProfileResourceFromEntityAssembler {
    public static UserProfileResource toResourceFromEntity(UserProfile entity) {
        return new UserProfileResource(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone()
        );
    }
}

