package com.example.demo.model.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Commit {
    public String sha;
    public String url;
}
