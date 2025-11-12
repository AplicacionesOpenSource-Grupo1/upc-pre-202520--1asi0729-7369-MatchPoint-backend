package com.upc.matchpoint.iam.interfaces.rest.transform;

import com.upc.matchpoint.iam.domain.model.commands.SignInCommand;
import com.upc.matchpoint.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource signInResource) {
        return new SignInCommand(signInResource.username(), signInResource.password());
    }
}
