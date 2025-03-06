package pl.lechowicz.model;

import pl.lechowicz.client.model.Branch;

import java.util.List;

public record Repository(String name, String owner, List<Branch> branches) {
}
