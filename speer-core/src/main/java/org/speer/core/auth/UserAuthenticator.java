package org.speer.core.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import jakarta.inject.Inject;
import org.speer.core.db.UserDAO;
import org.speer.core.entities.User;

import java.util.Objects;
import java.util.Optional;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {

    private final UserDAO userDAO;

    @Inject
    public UserAuthenticator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials basicCredentials) {
        User user = userDAO.findByUsername(basicCredentials.getUsername());
        if(Objects.isNull(user)){
            return Optional.empty();
        }
        if (user.getUsername().equals(basicCredentials.getUsername()) && user.getPassword().equals(basicCredentials.getPassword())) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
}
