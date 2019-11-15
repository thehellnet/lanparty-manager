package org.thehellnet.lanparty.manager.configuration;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class RestConfiguration extends RepositoryRestMvcConfiguration {

    public static final String API_BASE_PATH = "/api/public/rest";

    public RestConfiguration(ApplicationContext context, ObjectFactory<ConversionService> conversionService) {
        super(context, conversionService);
    }

    @Override
    public RepositoryRestConfiguration repositoryRestConfiguration() {
        return super.repositoryRestConfiguration()
                .setBasePath(API_BASE_PATH);
    }
}
