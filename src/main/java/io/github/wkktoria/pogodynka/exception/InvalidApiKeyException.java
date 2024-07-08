package io.github.wkktoria.pogodynka.exception;

public class InvalidApiKeyException extends Exception {
    public InvalidApiKeyException() {
        super("Invalid API key");
    }

    public InvalidApiKeyException(String message) {
        super(message);
    }
}
