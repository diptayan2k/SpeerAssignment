package org.speer.server;

import com.google.inject.Injector;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.PermitAllAuthorizer;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.speer.core.SpeerCoreConfig;
import org.speer.core.SpeerCoreModule;
import org.speer.core.auth.TokenAuthenticator;
import org.speer.core.auth.UserAuthenticator;
import org.speer.core.entities.Note;
import org.speer.core.entities.User;
import org.speer.core.resources.AuthResource;
import org.speer.core.resources.NoteResource;
import org.speer.core.util.RateLimiter;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorLookup;

import java.util.Arrays;


@Slf4j
public class SpeerApplication extends Application<SpeerConfiguration> {

    private final HibernateBundle<SpeerCoreConfig> hibernateBundle =
            new HibernateBundle<>(User.class, Note.class) {
                @Override
                public PooledDataSourceFactory getDataSourceFactory(SpeerCoreConfig speerCoreConfig) {
                    return speerCoreConfig.getDataSourceFactory();
                }
            };


    public static void main(final String[] args) throws Exception {
        new SpeerApplication().run(args);
    }

    @Override
    public String getName() {
        return "true";
    }

    @Override
    public void initialize(final Bootstrap<SpeerConfiguration> bootstrap) {
        // TODO: application initialization
        bootstrap.addBundle(hibernateBundle);
        GuiceBundle guiceBundle = GuiceBundle
                .builder()
                .modules(new SpeerCoreModule<SpeerConfiguration>(hibernateBundle){})
                .build();

        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final SpeerConfiguration configuration,
                    final Environment environment) {
        RateLimiter.init(configuration.getPermitsPerSecond());
        //createTableIfNotExists(userDAO, environment);
        // Register your resources with the Jersey environment
        Injector injector = InjectorLookup.getInjector(this).get();
        environment.jersey().register(injector.getInstance(AuthResource.class));
        environment.jersey().register(injector.getInstance(NoteResource.class));
        // registering the auth feature
// registering the auth feature
        environment.jersey().register(new AuthDynamicFeature(
                new ChainedAuthFilter<>(
                        Arrays.asList(
                                new BasicCredentialAuthFilter.Builder<User>()
                                        .setAuthenticator(injector.getInstance(UserAuthenticator.class))
                                        .setAuthorizer(new PermitAllAuthorizer<>())
                                        .setRealm("My Realm")
                                        .buildAuthFilter(),
                                new OAuthCredentialAuthFilter.Builder<User>()
                                        .setAuthenticator(injector.getInstance(TokenAuthenticator.class))
                                        .setAuthorizer(new PermitAllAuthorizer<>())
                                        .setPrefix("Bearer")
                                        .buildAuthFilter()
                        )
                )
        ));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));


    }

}
