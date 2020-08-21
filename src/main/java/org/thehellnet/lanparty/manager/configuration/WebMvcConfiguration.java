package org.thehellnet.lanparty.manager.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thehellnet.lanparty.manager.configuration.mixin.TestAwareConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements TestAwareConfiguration, WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    @Autowired
    public WebMvcConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(prepareStringConverter());
        converters.add(prepareJacksonConverter());
    }

    private StringHttpMessageConverter prepareStringConverter() {
        return new StringHttpMessageConverter();
    }

    private MappingJackson2HttpMessageConverter prepareJacksonConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());

        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(new MediaType("application", "*+json"));
        supportedMediaTypes.add(new MediaType("application", "json+*"));

        converter.setSupportedMediaTypes(supportedMediaTypes);

        return converter;
    }
}
