package org.springboot.example.upgrade;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepositoryController {
    private final StudentRepoPagingAndSorting studentRepoPagingAndSorting;
    private final StudentRepoReactiveSorting studentRepoReactiveSorting;
    private final StudentRepoRxJava3Sorting studentRepoRxJava3Sorting;

    public RepositoryController(StudentRepoPagingAndSorting studentRepoPagingAndSorting, StudentRepoReactiveSorting studentRepoReactiveSorting, StudentRepoRxJava3Sorting studentRepoRxJava3Sorting) {
        this.studentRepoPagingAndSorting = studentRepoPagingAndSorting;
        this.studentRepoReactiveSorting = studentRepoReactiveSorting;
        this.studentRepoRxJava3Sorting = studentRepoRxJava3Sorting;
    }

    public void actWithRepositories() {
        studentRepoPagingAndSorting.save(new Student<String>());
        List.of(new Student<String>())
                .forEach(studentRepoReactiveSorting::save);
        studentRepoRxJava3Sorting.save(new Student<String>());
    }
}
