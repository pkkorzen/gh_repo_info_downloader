package com.example.demo.paginatedresults;

import com.example.demo.model.repo.Repo;
import org.springframework.web.client.RestTemplate;

public class ReposPaginatedResult extends PaginatedResult<Repo> {

    @Override
    void createResponseEntity(RestTemplate restTemplate) {
        responseEntity = restTemplate.getForEntity(url, Repo[].class);
    }
}
