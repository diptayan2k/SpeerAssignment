package org.speer.core;


import io.dropwizard.db.DataSourceFactory;

public interface SpeerCoreConfig {

    public DataSourceFactory getDataSourceFactory();

    public String getSecretKey();
}
