package aw.geez.exception.converter;

import io.swagger.client.model.Currency;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;

import static aw.geez.utils.Constants.BIG_DECIMAL_TYPE_MISMATCH;
import static aw.geez.utils.Constants.CURRENCY_NOT_AVAILABLE;

@Component
public class MethodArgumentTypeMismatchConverter {
    public String convertToMessage(MethodArgumentTypeMismatchException e) {
        if (Currency.class.equals(e.getRequiredType())) {
            return CURRENCY_NOT_AVAILABLE.formatted(e.getValue());
        } else if (BigDecimal.class.equals(e.getRequiredType())) {
            return BIG_DECIMAL_TYPE_MISMATCH;
        }
        return e.getMessage();
    }
}

