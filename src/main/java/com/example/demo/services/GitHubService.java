package com.example.demo.services;

import com.example.demo.model.branch.Branch;
import com.example.demo.model.repo.GithubRepo;

import java.util.List;

public interface GitHubService {
    List<GithubRepo> findReposByUsername(String username);
    List<Branch> findBranchesByUsernameAndRepo(String username, GithubRepo repo);
}
