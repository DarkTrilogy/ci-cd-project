package aw.geez.dto.request;

import io.swagger.client.model.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewAccountDto {
    @NotNull
    private Integer customerId;

    @NotNull
    private Currency currency;
}
