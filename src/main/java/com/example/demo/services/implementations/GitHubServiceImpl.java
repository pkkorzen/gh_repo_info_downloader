package com.example.demo.services.implementations;

import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.model.repo.GithubRepo;
import com.example.demo.services.GitHubService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GitHubServiceImpl implements GitHubService {
    @Override
    public List<GitHubRepoDTO> findReposByUsername(String username) {
        return Collections.emptyList();
    }
    @Override
    public List<GitHubBranchDTO> findBranchesByUsernameAndRepo(String username, GithubRepo repo) {
        return List.of();
    }
}
