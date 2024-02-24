package com.example.demo.services.implementations;

import com.example.demo.converters.GitHubBranchDtoConverter;
import com.example.demo.converters.GitHubRepoDtoConverter;
import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.model.branch.Branch;
import com.example.demo.model.repo.Repo;
import com.example.demo.paginatedresults.BranchPaginatedResult;
import com.example.demo.paginatedresults.PaginatedResult;
import com.example.demo.paginatedresults.ReposPaginatedResult;
import com.example.demo.services.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubServiceImpl implements GitHubService {
    @Value("${github.api.address}")
    private String gitHubApiAddress;

    @Value("${results.per.page}")
    private String resultsPerPage;

    private final RestTemplate restTemplate;

    private final GitHubRepoDtoConverter gitHubRepoDtoConverter;

    private final GitHubBranchDtoConverter gitHubBranchDtoConverter;

    @Autowired
    public GitHubServiceImpl(RestTemplateBuilder restTemplateBuilder, GitHubRepoDtoConverter gitHubRepoDtoConverter,
                             GitHubBranchDtoConverter gitHubBranchDtoConverter) {
        this.restTemplate = restTemplateBuilder.build();
        this.gitHubRepoDtoConverter = gitHubRepoDtoConverter;
        this.gitHubBranchDtoConverter = gitHubBranchDtoConverter;
    }

    @Override
    public List<GitHubRepoDTO> findReposByUsername(String username) throws UserNotFoundException {
        try {
            List<Repo> repos = getRepos(username);
            List<GitHubRepoDTO> gitHubRepoDTOS = new ArrayList<>();
            if (!repos.isEmpty()) {
                gitHubRepoDTOS = repos
                        .stream()
                        .filter(repo -> !repo.isFork())
                        .map(repo -> gitHubRepoDtoConverter.apply(repo,
                                findBranchesByUsernameAndRepo(repo.getOwner().getLogin(), repo.getName())))
                        .toList();
            }
            return gitHubRepoDTOS;
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new UserNotFoundException("GitHub user: " + username + " not found");
            } else {
                throw e;
            }
        }
    }

    private List<GitHubBranchDTO> findBranchesByUsernameAndRepo(String username, String repoName) {
        List<Branch> branches = getBranches(username, repoName);
        List<GitHubBranchDTO> gitHubBranchDTOS = new ArrayList<>();
        if (!branches.isEmpty()) {
            gitHubBranchDTOS = branches
                    .stream()
                    .map(gitHubBranchDtoConverter)
                    .toList();
        }
        return gitHubBranchDTOS;
    }

    private List<Repo> getRepos(String username) {
        String url = gitHubApiAddress + "/users/" + username + "/repos?per_page=" + resultsPerPage;
        PaginatedResult<Repo> reposPaginatedResult = new ReposPaginatedResult();
        return reposPaginatedResult.getPaginationResult(restTemplate, url);
    }

    private List<Branch> getBranches(String username, String repoName) {
        String url = gitHubApiAddress + "/repos/" + username + "/" + repoName + "/branches?per_page=" + resultsPerPage;
        PaginatedResult<Branch> branchPaginatedResult = new BranchPaginatedResult();
        return branchPaginatedResult.getPaginationResult(restTemplate, url);
    }
}
