package com.example.springboot24to25example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("from Task t where t.id=:id")
    Task getById(@Param("id") Long id);

}
