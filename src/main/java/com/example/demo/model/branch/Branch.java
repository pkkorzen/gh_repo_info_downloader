package com.example.demo.model.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Branch {
    private String name;
    private Commit commit;
    private boolean isMyProtected;
    private Protection protection;
    private String protectionUrl;
}
