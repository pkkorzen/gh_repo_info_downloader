package com.example.demo.converters;

import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.model.repo.GithubRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
public class GitHubRepoDtoConverter implements BiFunction<GithubRepo, List<GitHubBranchDTO>, GitHubRepoDTO> {

    @Override
    public GitHubRepoDTO apply(GithubRepo githubRepo, List<GitHubBranchDTO> gitHubBranchDTOs) {
        GitHubRepoDTO githubRepoDTO = new GitHubRepoDTO();
        githubRepoDTO.setRepositoryName(githubRepo.getName());
        githubRepoDTO.setOwnerLogin(githubRepo.getOwner().getLogin());
        githubRepoDTO.setBranches(gitHubBranchDTOs);
        return githubRepoDTO;
    }
}
