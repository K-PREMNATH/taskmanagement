package com.erconadjustment.taskmanagement.controller;

import com.erconadjustment.taskmanagement.dto.TaskRequest;
import com.erconadjustment.taskmanagement.entity.Task;
import com.erconadjustment.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAll(@RequestParam(required = false) Integer systemId) {
        if (systemId != null) {
            return taskService.findBySystem(systemId);
        }
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Integer id) {
        return taskService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody TaskRequest request) {
        Task created = taskService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Integer id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
