package com.example.demo.model.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Branch {
    public String name;
    public Commit commit;
    public boolean myprotected;
    public Protection protection;
    public String protection_url;
}
