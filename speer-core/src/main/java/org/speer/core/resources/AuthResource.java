package org.speer.core.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.ResponseMetered;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.speer.core.auth.TokenAuthenticator;
import org.speer.core.auth.SpeerTokenManager;
import org.speer.core.auth.UserAuthenticator;
import org.speer.core.db.UserDAO;
import org.speer.core.entities.User;
import org.speer.core.models.GenericResponse;

import java.util.Optional;


@Path("api/auth")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final UserDAO userDAO;
    private final TokenAuthenticator tokenAuthenticator;
    private final UserAuthenticator userAuthenticator;
    private final SpeerTokenManager tokenGenerator;
    @Inject
    public AuthResource(UserDAO userDAO,
                        TokenAuthenticator tokenAuthenticator,
                        SpeerTokenManager tokenGenerator,
                        UserAuthenticator userAuthenticator) {
        this.userDAO = userDAO;
        this.tokenAuthenticator = tokenAuthenticator;
        this.tokenGenerator = tokenGenerator;
        this.userAuthenticator = userAuthenticator;
    }

    @Path("/signup")
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @POST
    @UnitOfWork
    public GenericResponse<User> userSignup(@Valid User user){
        try {
            return GenericResponse.<User>builder()
                    .data(userDAO.create(user))
                    .success(true)
                    .message("User signed up successfully!!")
                    .build();
        } catch (Exception e){
            return GenericResponse.<User>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @Path("/{username}")
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @GET
    @UnitOfWork
    public GenericResponse getUser(@PathParam("username") String username){
        try {
            return GenericResponse.<User>builder()
                    .data(userDAO.findByUsername(username))
                    .success(true)
                    .message("User fetched successfully!!")
                    .build();
        } catch (Exception e){
            return GenericResponse.<User>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/login")
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @UnitOfWork
    public GenericResponse<String> login(@QueryParam("username") String username,
                          @QueryParam("password") String password) {
        // Authenticate the user
        Optional<User> optionalUser = userAuthenticator.authenticate(new BasicCredentials(username, password));

        if(optionalUser.isEmpty()){
            return GenericResponse
                    .<String>builder()
                    .success(false)
                    .message("Invalid credentials!!")
                    .build();
        }
        // Issue a JWT token
        String jwtToken = tokenGenerator.generateToken(optionalUser.get());
        // Return the token in the response
        return GenericResponse
                .<String>builder()
                .success(true)
                .message("Logged in successfully!!")
                .data(jwtToken)
                .build();

    }


}
