package fis.auth.infrastructure.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestConfiguration {

    @Bean
    @Qualifier("user-ms")
    public RestTemplate getRestTemplateUserMs(RestTemplateBuilder builder) {
        String baseUrl = "http://fis-usuario:8093";
        return builder
                .rootUri(baseUrl)
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(30))
                .build();
    }

}
