package org.speer.server;

import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.speer.core.SpeerCoreConfig;

@EqualsAndHashCode(callSuper = true)
@Data
public class SpeerConfiguration extends Configuration implements SpeerCoreConfig {

    @Valid
    @NotNull
    public DataSourceFactory dataSourceFactory;

    @Valid
    @NotNull
    String secretKey;
}
