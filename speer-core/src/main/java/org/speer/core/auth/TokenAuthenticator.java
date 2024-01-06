package org.speer.core.auth;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.speer.core.db.UserDAO;
import org.speer.core.entities.User;

import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
public class TokenAuthenticator implements Authenticator<String, User> {

    private  UserDAO userDAO;
    private  SpeerTokenManager tokenManager;


    @Inject
    public TokenAuthenticator(UserDAO userDAO, SpeerTokenManager tokenManager) {
        this.userDAO = userDAO;
        this.tokenManager = tokenManager;
    }

    @Override
    @UnitOfWork
    public Optional<User> authenticate(String token) {
        String username = validateAndParseToken(token);
        // Retrieve the user based on the username
        User user = userDAO.findByUsername(username);
        return Optional.ofNullable(user);
    }

    private String validateAndParseToken(String token) {
        return tokenManager.validateAndParseToken(token);
    }
}
