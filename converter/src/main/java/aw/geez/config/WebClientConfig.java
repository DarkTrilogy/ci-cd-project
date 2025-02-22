package aw.geez.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static aw.geez.utils.Constants.REGISTRATION_ID;

@Configuration
public class WebClientConfig {

    @Bean
    ReactiveClientRegistrationRepository getRegistration(
            @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}") String token_uri,
            @Value("${spring.security.oauth2.client.registration.converter.client-id}") String client_id,
            @Value("${spring.security.oauth2.client.registration.converter.client-secret}") String client_secret
    ) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId(REGISTRATION_ID)
                .tokenUri(token_uri)
                .clientId(client_id)
                .clientSecret(client_secret)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations) {
        var oauth = getServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations);
        oauth.setDefaultClientRegistrationId(REGISTRATION_ID);

        HttpClient client = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(20));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client))
                .filter(oauth)
                .build();
    }

    private static ServerOAuth2AuthorizedClientExchangeFilterFunction getServerOAuth2AuthorizedClientExchangeFilterFunction(ReactiveClientRegistrationRepository clientRegistrations) {
        InMemoryReactiveOAuth2AuthorizedClientService clientService =
                new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);
        return new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    }
}
