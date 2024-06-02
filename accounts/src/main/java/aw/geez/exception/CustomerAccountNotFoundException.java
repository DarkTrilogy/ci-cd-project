package aw.geez.exception;

import jakarta.persistence.EntityNotFoundException;

import static aw.geez.utils.Constants.ACCOUNT_NOT_FOUND;

public class CustomerAccountNotFoundException extends EntityNotFoundException {
    public CustomerAccountNotFoundException(String message) {
        super(message);
    }

    public CustomerAccountNotFoundException() {
        super(ACCOUNT_NOT_FOUND);
    }
}
