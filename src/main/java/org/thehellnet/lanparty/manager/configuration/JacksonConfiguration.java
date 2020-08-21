package org.thehellnet.lanparty.manager.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.thehellnet.lanparty.manager.configuration.mixin.TestAwareConfiguration;

@Configuration
public class JacksonConfiguration implements TestAwareConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder
                .json()
                .modules(
                        new Jdk8Module(),
                        new JavaTimeModule()
                )
                .featuresToEnable(
                        SerializationFeature.INDENT_OUTPUT,
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS,
                        SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS,
                        SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
                        SerializationFeature.INDENT_OUTPUT
                )
                .featuresToDisable(
                        SerializationFeature.FAIL_ON_EMPTY_BEANS
                )
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .build();
    }
}
