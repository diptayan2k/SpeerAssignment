package org.speer.core;

import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class SpeerCoreBundle<T> implements ConfiguredBundle<T> {

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }
    @Override
    public void run(T configuration, Environment environment) throws Exception {
    }

}
