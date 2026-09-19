package com.example.TestSpring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    /** Cliente apuntando a la API de Rick and Morty. */
    @Bean
    public RestClient rickAndMortyRestClient(
            RestClient.Builder builder,
            @Value("${rickandmorty.api.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
