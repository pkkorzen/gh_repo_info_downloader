package com.example.demo.exceptions.exceptionHandlers;

import com.example.demo.errors.GitHubError;
import com.example.demo.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GitHubExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        return buildResponseEntity(new GitHubError(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(GitHubError gitHubError) {
        return new ResponseEntity<>(gitHubError, HttpStatusCode.valueOf(gitHubError.getStatusCode()));
    }
}
