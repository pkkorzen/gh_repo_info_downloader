package com.example.demo.services;

import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;

import java.util.List;

public interface GitHubService {
    List<GitHubRepoDTO> findReposByUsername(String username);
    List<GitHubBranchDTO> findBranchesByUsernameAndRepo(String username, String repoName);
}
