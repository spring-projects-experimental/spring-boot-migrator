package org.springboot.example.upgrade;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StudentRepoPagingAndSorting extends PagingAndSortingRepository<Student, Long> {
}
