package aw.geez.service;

import aw.geez.client.RatesClient;
import aw.geez.dto.CurrencyResponseDto;
import aw.geez.exception.CurrencyNotFoundException;
import aw.geez.exception.NotPositiveAmountException;
import io.swagger.client.model.RatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import io.swagger.client.model.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final RatesClient ratesClient;

    public CurrencyResponseDto convert(Currency from, Currency to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotPositiveAmountException();
        }
        RatesResponse rates = ratesClient.getRatesResponse();
        BigDecimal amountConverted = convertRate(rates, from, to, amount);
        return new CurrencyResponseDto(to, amountConverted);
    }

    private BigDecimal convertRate(RatesResponse rates, Currency from, Currency to, BigDecimal amount) {
        Map<String, BigDecimal> ratesMap = rates.getRates();
        BigDecimal fromRateToBase = ratesMap.get(from.toString());
        BigDecimal toRateToBase = ratesMap.get(to.toString());
        if (fromRateToBase == null) throw new CurrencyNotFoundException(from.name());
        if (toRateToBase == null) throw new CurrencyNotFoundException(to.name());
        return fromRateToBase.multiply(amount).divide(toRateToBase, 2, RoundingMode.HALF_EVEN);
    }
}
