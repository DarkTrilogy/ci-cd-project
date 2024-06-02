package aw.geez.exception;

import static aw.geez.utils.Constants.AMOUNT_NOT_POSITIVE;

public class NotPositiveAmountException extends CurrencyException {
    public NotPositiveAmountException(String message) {
        super(message);
    }

    public NotPositiveAmountException() {
        super(AMOUNT_NOT_POSITIVE);
    }
}
