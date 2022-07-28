package org.springboot.example.upgrade;

import org.springframework.data.repository.reactive.RxJava3SortingRepository;

public interface StudentUncalledRepoRxJava3Sorting extends RxJava3SortingRepository<Student<?>, Long> {
}
