package aw.geez.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewCustomerResponse {
    private Integer customerId;
}