package aw.geez.dto;

import io.swagger.client.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public final class CurrencyResponseDto {
    Currency currency;
    BigDecimal amount;
}
