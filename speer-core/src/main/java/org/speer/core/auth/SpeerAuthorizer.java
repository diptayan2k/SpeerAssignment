package org.speer.core.auth;

import io.dropwizard.auth.Authorizer;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.speer.core.entities.User;

public class SpeerAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String s, @Nullable ContainerRequestContext containerRequestContext) {
        return false;
    }
}
