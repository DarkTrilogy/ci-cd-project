package aw.geez.dto.response;

import io.swagger.client.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponse {
    private Currency currency;
    private BigDecimal amount;
}
