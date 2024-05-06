package com.bw.pce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductEnrichException extends Exception {

    public ProductEnrichException() {
    }

    public ProductEnrichException(String message) {
        super(message);
    }

    public ProductEnrichException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductEnrichException(Throwable cause) {
        super(cause);
    }
}
