/*
 * Copyright 2021 VMware, Inc.
 * SPDX-License-Identifier: Apache License 2.0
 *
 * @author: fkrueger
 */
package com.example.springboot24to25example;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TagService tagService;

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable("id") Long id) {
        Task task = taskService.getTask(id);
        return task;
    }

    @GetMapping("/tags/{id}")
    public Tag getTag(@PathVariable("id") Long id) {
        Tag tag = tagService.getTag(id);
        return tag;
    }
}