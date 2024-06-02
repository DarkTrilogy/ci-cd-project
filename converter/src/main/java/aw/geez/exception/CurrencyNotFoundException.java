package aw.geez.exception;

import static aw.geez.utils.Constants.CURRENCY_NOT_AVAILABLE;

public class CurrencyNotFoundException extends CurrencyException {
    public CurrencyNotFoundException(String message) {
        super(CURRENCY_NOT_AVAILABLE.formatted(message));
    }
}
