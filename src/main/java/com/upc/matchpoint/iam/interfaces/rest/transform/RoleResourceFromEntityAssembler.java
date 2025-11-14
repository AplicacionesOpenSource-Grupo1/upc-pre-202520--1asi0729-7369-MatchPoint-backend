package com.upc.matchpoint.iam.interfaces.rest.transform;

import com.upc.matchpoint.iam.domain.model.entities.Role;
import com.upc.matchpoint.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role role) {
        return new RoleResource(role.getId(), role.getStringName());
    }
}
