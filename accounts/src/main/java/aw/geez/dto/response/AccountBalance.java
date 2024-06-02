package aw.geez.dto.response;

import io.swagger.client.model.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountBalance {
    private BigDecimal balance;
    private Currency currency;
}
