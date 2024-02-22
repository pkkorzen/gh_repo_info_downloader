package com.example.demo.errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubError {

    private int statusCode;
    private String message;

    public GitHubError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
