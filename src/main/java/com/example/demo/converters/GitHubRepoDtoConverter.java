package com.example.demo.converters;

import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.model.repo.Repo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
public class GitHubRepoDtoConverter implements BiFunction<Repo, List<GitHubBranchDTO>, GitHubRepoDTO> {

    @Override
    public GitHubRepoDTO apply(Repo repo, List<GitHubBranchDTO> gitHubBranchDTOs) {
        GitHubRepoDTO githubRepoDTO = new GitHubRepoDTO();
        githubRepoDTO.setRepositoryName(repo.getName());
        githubRepoDTO.setOwnerLogin(repo.getOwner().getLogin());
        githubRepoDTO.setBranches(gitHubBranchDTOs);
        return githubRepoDTO;
    }
}
