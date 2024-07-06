package io.github.wkktoria.pogodynka.exception;

public class InvalidLocationException extends Exception {
    public InvalidLocationException() {
        super("Invalid location");
    }

    public InvalidLocationException(String message) {
        super(message);
    }
}
