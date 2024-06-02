package aw.geez.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferRequest {
    @NotNull
    private Integer receiverAccount;

    @NotNull
    private Integer senderAccount;

    @NotNull
    private BigDecimal transferAmount;
}
