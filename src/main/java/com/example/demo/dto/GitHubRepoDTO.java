package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class GitHubRepoDTO {
    private String repositoryName;
    private String ownerLogin;
    private List<GitHubBranchDTO> branches;
}
