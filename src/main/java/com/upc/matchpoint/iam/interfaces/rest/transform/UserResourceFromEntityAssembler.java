package com.upc.matchpoint.iam.interfaces.rest.transform;

import com.upc.matchpoint.iam.domain.model.aggregates.User;
import com.upc.matchpoint.iam.domain.model.entities.Role;
import com.upc.matchpoint.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        var roles = user.getRoles().stream().map(Role::getStringName).toList();
        return new UserResource(user.getId(), user.getUsername(), roles);
    }
}
