package pl.lechowicz.restservice.exception.model;

public record ErrorResponse(int status, String message) {
}
