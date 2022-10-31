package org.springboot.example.upgrade;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepositoryController {
    private final StudentRepoPagingAndSorting studentRepoPagingAndSorting;

    public RepositoryController(StudentRepoPagingAndSorting studentRepoPagingAndSorting) {
        this.studentRepoPagingAndSorting = studentRepoPagingAndSorting;
    }

    public void actWithRepositories() {
        studentRepoPagingAndSorting.save(new Student());
    }
}
