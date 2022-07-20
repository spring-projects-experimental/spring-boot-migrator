package org.springboot.example.upgrade;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StudentRepo extends PagingAndSortingRepository<Student<?>, Long> {
}
