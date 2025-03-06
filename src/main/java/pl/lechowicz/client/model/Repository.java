package pl.lechowicz.client.model;

public record Repository(String name, Owner owner, boolean fork) {
}
