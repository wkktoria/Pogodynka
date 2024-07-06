package io.github.wkktoria.pogodynka.exception;

public class ApiProblemException extends Exception {
    public ApiProblemException() {
        super("API problem");
    }

    public ApiProblemException(String message) {
        super(message);
    }
}
