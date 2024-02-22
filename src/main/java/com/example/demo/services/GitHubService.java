package com.example.demo.services;

import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.exceptions.UserNotFoundException;

import java.util.List;

public interface GitHubService {
    List<GitHubRepoDTO> findReposByUsername(String username) throws UserNotFoundException;
    List<GitHubBranchDTO> findBranchesByUsernameAndRepo(String username, String repoName);
}
