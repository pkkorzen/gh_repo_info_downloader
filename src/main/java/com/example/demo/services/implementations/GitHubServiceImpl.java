package com.example.demo.services.implementations;

import com.example.demo.converters.GitHubBranchDtoConverter;
import com.example.demo.converters.GitHubRepoDtoConverter;
import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.model.branch.Branch;
import com.example.demo.model.repo.GithubRepo;
import com.example.demo.services.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class GitHubServiceImpl implements GitHubService {
    @Value("${github.api.address}")
    private String gitHubApiAddress;

    private final RestTemplate restTemplate;

    @Autowired
    private GitHubRepoDtoConverter gitHubRepoDtoConverter;

    @Autowired
    private GitHubBranchDtoConverter gitHubBranchDtoConverter;

    public GitHubServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<GitHubRepoDTO> findReposByUsername(String username) throws UserNotFoundException {
        try {
            ResponseEntity<GithubRepo[]> responseEntity = restTemplate.getForEntity(gitHubApiAddress + "/users/" +
                    username + "/repos", GithubRepo[].class);
            GithubRepo[] githubReposArray = responseEntity.getBody();
            List<GitHubRepoDTO> gitHubRepoDTOS = new ArrayList<>();
            if (Objects.nonNull(githubReposArray)) {
                gitHubRepoDTOS = Arrays
                        .stream(githubReposArray)
                        .filter(repo -> !repo.isFork())
                        .map(repo -> gitHubRepoDtoConverter.apply(repo,
                                findBranchesByUsernameAndRepo(repo.getOwner().getLogin(), repo.getName())))
                        .toList();
            }
            return gitHubRepoDTOS;
        } catch (final HttpClientErrorException e) {
            throw new UserNotFoundException("GitHub user: " + username + " not found");
        }
    }

    private List<GitHubBranchDTO> findBranchesByUsernameAndRepo(String username, String repoName) {
        ResponseEntity<Branch[]> responseEntity = restTemplate.getForEntity(gitHubApiAddress + "/repos/" +
                username + "/" + repoName + "/branches", Branch[].class);
        Branch[] branchesArray = responseEntity.getBody();
        List<GitHubBranchDTO> gitHubBranchDTOS = new ArrayList<>();
        if (Objects.nonNull(branchesArray)) {
            gitHubBranchDTOS = Arrays
                    .stream(branchesArray)
                    .map(branch -> gitHubBranchDtoConverter.apply(branch))
                    .toList();
        }
        return gitHubBranchDTOS;
    }
}
