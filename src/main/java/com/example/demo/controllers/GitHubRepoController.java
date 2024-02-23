package com.example.demo.controllers;

import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.services.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GitHubRepoController {

    private final GitHubService gitHubService;

    @Autowired
    public GitHubRepoController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @RequestMapping(value = "/repos/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<GitHubRepoDTO> getUserRepos(@PathVariable String username) throws UserNotFoundException {
        return gitHubService.findReposByUsername(username);
    }
}
