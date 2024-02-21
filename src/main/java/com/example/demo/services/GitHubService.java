package com.example.demo.services;

import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.model.repo.GithubRepo;

import java.util.List;

public interface GitHubService {
    List<GitHubRepoDTO> findReposByUsername(String username);
    List<GitHubBranchDTO> findBranchesByUsernameAndRepo(String username, GithubRepo repo);
}
