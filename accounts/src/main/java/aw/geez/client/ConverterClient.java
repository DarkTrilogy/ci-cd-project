package aw.geez.client;

import aw.geez.dto.response.CurrencyResponse;
import aw.geez.exception.NullBodyResponseException;
import io.swagger.client.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class ConverterClient {
    private final WebClient webClient;
    @Value("${converter-service.converter-path}")
    private final String converterPath;

    public CurrencyResponse convertCurrency(Currency from, Currency to, BigDecimal amount) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(converterPath)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("amount", amount);
        return webClient.get()
                .uri(builder.build().toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CurrencyResponse.class)
                .onErrorResume(error -> Mono.error(new NullBodyResponseException()))
                .block();
    }
}
