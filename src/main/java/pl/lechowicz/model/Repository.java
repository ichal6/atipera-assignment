package pl.lechowicz.model;

import java.util.List;

public record Repository(String name, String owner, List<Branch> branches) {
}
