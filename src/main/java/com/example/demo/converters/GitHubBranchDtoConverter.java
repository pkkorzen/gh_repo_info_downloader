package com.example.demo.converters;

import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.model.branch.Branch;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class GitHubBranchDtoConverter implements Function<Branch, GitHubBranchDTO> {

    @Override
    public GitHubBranchDTO apply(Branch branch) {
        GitHubBranchDTO githubBranchDTO = new GitHubBranchDTO();
        githubBranchDTO.setName(branch.getName());
        githubBranchDTO.setCommitSha(branch.getCommit().getSha());
        return githubBranchDTO;
    }
}
