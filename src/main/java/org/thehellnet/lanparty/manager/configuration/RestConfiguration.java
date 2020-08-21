package org.thehellnet.lanparty.manager.configuration;

import org.reflections.Reflections;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.thehellnet.lanparty.manager.configuration.mixin.TestAwareConfiguration;
import org.thehellnet.lanparty.manager.model.persistence.AbstractEntity;

import java.util.Set;

@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class RestConfiguration extends RepositoryRestMvcConfiguration implements TestAwareConfiguration {

    public static final String API_BASE_PATH = "/api/public/rest";

    public RestConfiguration(ApplicationContext context, ObjectFactory<ConversionService> conversionService) {
        super(context, conversionService);
    }

    @Override
    public RepositoryRestConfiguration repositoryRestConfiguration() {
        return super.repositoryRestConfiguration()
                .setBasePath(API_BASE_PATH)
                .exposeIdsFor(getEntityClasses());
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    private Class<?>[] getEntityClasses() {
        Reflections reflections = new Reflections("org.thehellnet.lanparty.manager.model.persistence");
        Set<Class<? extends AbstractEntity>> classes = reflections.getSubTypesOf(AbstractEntity.class);
        return classes.toArray(new Class[0]);
    }
}
