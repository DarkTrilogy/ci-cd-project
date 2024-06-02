package aw.geez.exception;

import static aw.geez.utils.Constants.NULL_BODY_RESPONSE;

public class NullBodyResponseException extends RuntimeException {
    public NullBodyResponseException(String message) {
        super(message);
    }

    public NullBodyResponseException() {
        super(NULL_BODY_RESPONSE);
    }
}
