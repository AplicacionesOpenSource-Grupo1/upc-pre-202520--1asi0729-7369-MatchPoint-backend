package com.upc.matchpoint.iam.interfaces.rest.transform;

import com.upc.matchpoint.iam.domain.model.aggregates.User;
import com.upc.matchpoint.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(user.getId(), user.getUsername(), token);
    }
}
