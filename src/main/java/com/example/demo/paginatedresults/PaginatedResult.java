package com.example.demo.paginatedresults;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

public abstract class PaginatedResult<T> {
    protected String url;
    private static final String NEXT_PATTERN = "rel=\"next\"";
    private boolean pagesRemaining = true;
    private final List<T> results = new ArrayList<>();
    protected ResponseEntity<T[]> responseEntity;
    private T[] resultsArray;
    private List<String> links;
    private Map<String, String> linksMap;

    public List<T> getPaginationResult(RestTemplate restTemplate, String url) {
        this.url = url;
        while (pagesRemaining) {
            createResponseEntity(restTemplate);
            getBody();
            transformArrayIntoList();
            getHeadersLinks();
            createLinksMap();
            checkIfPagesRemaining();
            updateUrl();
        }
        return results;
    }

    abstract void createResponseEntity(RestTemplate restTemplate);

    private void getBody() {
        resultsArray = responseEntity.getBody();
    }

    private void transformArrayIntoList() {
        if (isArrayPopulated(resultsArray)) {
            results.addAll(Arrays.asList(resultsArray));
        }
    }

    private static <T> boolean isArrayPopulated(T[] array) {
        return Objects.nonNull(array);
    }

    private void getHeadersLinks() {
        links = responseEntity.getHeaders().get("Link");
    }

    private void createLinksMap() {
        if (Objects.nonNull(links)) {
            linksMap = Arrays.stream(links.getFirst().split(","))
                    .toList()
                    .stream()
                    .collect(Collectors.toMap(link -> link.split(";")[1].replace(" ", ""),
                            link -> link.split(";")[0]));
        }
    }

    private void checkIfPagesRemaining() {
        pagesRemaining = Objects.nonNull(links) && !links.isEmpty() && linksMap.containsKey(NEXT_PATTERN);
    }

    private void updateUrl() {
        if (pagesRemaining) {
            url = createUrl(linksMap);
        }
    }

    private static String createUrl(Map<String, String> linksMap) {
        return linksMap.get(NEXT_PATTERN)
                .replace("<", "")
                .replace(">", "")
                .replace(" ", "");
    }
}
