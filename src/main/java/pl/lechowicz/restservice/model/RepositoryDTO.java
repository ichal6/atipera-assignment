package pl.lechowicz.restservice.model;

import java.util.List;

public record RepositoryDTO(String name, String ownerLogin, List<BranchDTO> branches) {
}
