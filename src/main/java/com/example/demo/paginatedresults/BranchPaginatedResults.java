package com.example.demo.paginatedresults;

import com.example.demo.model.branch.Branch;
import org.springframework.web.client.RestTemplate;

public class BranchPaginatedResults extends PaginatedResult<Branch> {

    @Override
    void createResponseEntity(RestTemplate restTemplate) {
        responseEntity = restTemplate.getForEntity(url, Branch[].class);
    }
}
