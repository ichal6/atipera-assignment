package pl.lechowicz.restservice.dto;

import java.util.List;

public record RepositoryDTO(String name, String ownerLogin, List<BranchDTO> branches) {
}
