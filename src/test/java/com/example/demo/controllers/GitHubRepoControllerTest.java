package com.example.demo.controllers;

import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.services.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GitHubRepoController.class)
class GitHubRepoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GitHubService service;

    String END_POINT_PATH = "/repos";

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        String username = "notExistingUser";
        String requestURI = END_POINT_PATH + "/" + username;

        Mockito.when(service.findReposByUsername(username))
                .thenThrow(new UserNotFoundException("Github user: "+username+" not found"));

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetShouldReturn200OK() throws Exception {
        String username = "existingUser";
        String requestURI = END_POINT_PATH + "/" + username;

        List<GitHubRepoDTO> gitHubRepoDTOS = getGitHubRepoDTOS(username);

        Mockito.when(service.findReposByUsername(username)).thenReturn(gitHubRepoDTOS);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[1].name").value("repoName1"))
                .andExpect(jsonPath("$[1].ownerLogin").value(username))
                .andExpect(jsonPath("$[1].branches").exists())
                .andExpect(jsonPath("$[1].branches[0].name").value("master"))
                .andExpect(jsonPath("$[1].branches[0].commitSha").value("1234"));
    }

    private static List<GitHubRepoDTO> getGitHubRepoDTOS(String username) {
        List<GitHubRepoDTO> gitHubRepoDTOS = new ArrayList<>();

        GitHubBranchDTO gitHubBranchDTO = new GitHubBranchDTO();
        gitHubBranchDTO.setBranchName("master");
        gitHubBranchDTO.setLastCommitSha("1234");
        GitHubBranchDTO gitHubBranchDTO2 = new GitHubBranchDTO();
        gitHubBranchDTO2.setBranchName("feature");
        gitHubBranchDTO2.setLastCommitSha("5678");
        List<GitHubBranchDTO> gitHubBranchDTOS = new ArrayList<>();
        gitHubBranchDTOS.add(gitHubBranchDTO);
        gitHubBranchDTOS.add(gitHubBranchDTO2);

        for (int i = 0; i < 4; i++) {
            GitHubRepoDTO gitHubRepoDTO = new GitHubRepoDTO();
            gitHubRepoDTO.setOwnerLogin(username);
            gitHubRepoDTO.setRepositoryName("repoName" +i);
            gitHubRepoDTO.setBranches(gitHubBranchDTOS);
            gitHubRepoDTOS.add(gitHubRepoDTO);
        }
        return gitHubRepoDTOS;
    }
}