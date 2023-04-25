package org.springboot.example.upgrade;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface StudentRepoReactiveSorting extends ReactiveSortingRepository<Student, Long> {
}
