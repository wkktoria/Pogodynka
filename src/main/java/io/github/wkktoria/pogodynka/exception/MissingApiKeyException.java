package io.github.wkktoria.pogodynka.exception;

public class MissingApiKeyException extends Exception {
    public MissingApiKeyException() {
        super("API key is missing");
    }

    public MissingApiKeyException(String message) {
        super(message);
    }
}
