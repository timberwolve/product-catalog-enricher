package com.bw.pce.exceptions;

public class LoadingProductsDictionaryException extends Exception {

    public LoadingProductsDictionaryException() {
    }

    public LoadingProductsDictionaryException(String message) {
        super(message);
    }

    public LoadingProductsDictionaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadingProductsDictionaryException(Throwable cause) {
        super(cause);
    }
}