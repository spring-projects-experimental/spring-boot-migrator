package org.springframework.sbm.scopeplayground;

import lombok.Getter;

@Getter
public class ProjectMetadata {
    private String metadata;

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}