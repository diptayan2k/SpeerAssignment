package org.speer.core;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.core.Configuration;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.SessionFactory;
import org.speer.core.auth.SpeerTokenManager;
import org.speer.core.auth.TokenAuthenticator;
import org.speer.core.db.UserDAO;
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorLookup;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SpeerCoreModule<T extends Configuration & SpeerCoreConfig> extends AbstractModule {

    private final HibernateBundle<SpeerCoreConfig> hibernateBundle;


    @Override
    protected void configure(){

    }

    @Provides
    @Singleton
    public SessionFactory getSessionFactory(){
        return hibernateBundle.getSessionFactory();
    }

    @Provides
    @Singleton
    @Named("secretKey")
    public String getSecretKey(T config){
        return config.getSecretKey();
    }

    @Provides
    @Singleton
    public TokenAuthenticator getTokenAuthenticator(UserDAO userDAO, SpeerTokenManager speerTokenManager){
        return new UnitOfWorkAwareProxyFactory(hibernateBundle).create(TokenAuthenticator.class,
                new Class[]{UserDAO.class, SpeerTokenManager.class}, new Object[]{userDAO, speerTokenManager});
    }

}
