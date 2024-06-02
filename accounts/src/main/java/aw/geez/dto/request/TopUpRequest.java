package aw.geez.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class TopUpRequest {

    @NotNull
    private BigDecimal amount;
}
