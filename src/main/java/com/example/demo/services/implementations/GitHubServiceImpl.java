package com.example.demo.services.implementations;

import com.example.demo.converters.GitHubBranchDtoConverter;
import com.example.demo.converters.GitHubRepoDtoConverter;
import com.example.demo.dto.GitHubBranchDTO;
import com.example.demo.dto.GitHubRepoDTO;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.model.branch.Branch;
import com.example.demo.model.repo.Repo;
import com.example.demo.services.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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
        String nextPattern = "rel=\"next\"";
        boolean pagesRemaining = true;
        List<Repo> repos = new ArrayList<>();

        while (pagesRemaining) {
            ResponseEntity<Repo[]> responseEntity = restTemplate.getForEntity(url, Repo[].class);
            Repo[] reposArray = responseEntity.getBody();

            transformArrayIntoList(reposArray, repos);

            List<String> links = getHeadersLinks(responseEntity);
            Map<String, String> linksMap = createLinksMap(links);

            pagesRemaining = arePagesRemaining(links, linksMap, nextPattern);

            url = updateUrl(pagesRemaining, url, linksMap, nextPattern);
        }
        return repos;
    }

    private List<Branch> getBranches(String username, String repoName) {
        String url = gitHubApiAddress + "/repos/" + username + "/" + repoName + "/branches?per_page=" + resultsPerPage;
        String nextPattern = "rel=\"next\"";
        boolean pagesRemaining = true;
        List<Branch> branches = new ArrayList<>();

        while (pagesRemaining) {
            ResponseEntity<Branch[]> responseEntity = restTemplate.getForEntity(url, Branch[].class);
            Branch[] branchesArray = responseEntity.getBody();

            transformArrayIntoList(branchesArray, branches);

            List<String> links = getHeadersLinks(responseEntity);
            Map<String, String> linksMap = createLinksMap(links);

            pagesRemaining = arePagesRemaining(links, linksMap, nextPattern);

            url = updateUrl(pagesRemaining, url, linksMap, nextPattern);
        }
        return branches;
    }

    private static String updateUrl(boolean pagesRemaining, String url, Map<String, String> linksMap, String nextPattern) {
        if (pagesRemaining) {
            url = createUrl(linksMap, nextPattern);
        }
        return url;
    }

    private static <T> void transformArrayIntoList(T[] array, List<T> list) {
        if (isArrayPopulated(array)) {
            list.addAll(Arrays.asList(array));
        }
    }

    private static <T> boolean isArrayPopulated(T[] array) {
        return Objects.nonNull(array);
    }

    private static Map<String, String> createLinksMap(List<String> links) {
        Map<String, String> linksMap = new HashMap<>();
        if (Objects.nonNull(links)) {
            linksMap = Arrays.stream(links.getFirst().split(","))
                    .toList()
                    .stream()
                    .collect(Collectors.toMap(link -> link.split(";")[1].replace(" ", ""),
                            link -> link.split(";")[0]));
        }
        return linksMap;
    }

    private static String createUrl(Map<String, String> linksMap, String nextPattern) {
        return linksMap.get(nextPattern)
                .replace("<", "")
                .replace(">", "")
                .replace(" ", "");
    }

    private static boolean arePagesRemaining(List<String> links, Map<String, String> linksMap, String nextPattern) {
        return Objects.nonNull(links) && !links.isEmpty() && linksMap.containsKey(nextPattern);
    }

    private static <T> List<String> getHeadersLinks(ResponseEntity<T[]> responseEntity) {
        return responseEntity.getHeaders().get("Link");
    }
}
