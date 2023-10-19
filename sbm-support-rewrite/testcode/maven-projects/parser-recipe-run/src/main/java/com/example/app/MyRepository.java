package com.example.app;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyRepository extends JpaRepository<My, Long> {

}