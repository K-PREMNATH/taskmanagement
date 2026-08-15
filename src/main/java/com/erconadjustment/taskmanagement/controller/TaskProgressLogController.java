package com.erconadjustment.taskmanagement.controller;

import com.erconadjustment.taskmanagement.dto.TaskProgressLogRequest;
import com.erconadjustment.taskmanagement.entity.TaskProgressLog;
import com.erconadjustment.taskmanagement.service.TaskProgressLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class TaskProgressLogController {

    @Autowired
    private TaskProgressLogService progressService;

    @GetMapping
    public List<TaskProgressLog> getAll(@RequestParam(required = false) Integer assignmentId) {
        if (assignmentId != null) {
            return progressService.findByAssignment(assignmentId);
        }
        return progressService.findAll();
    }

    @GetMapping("/{id}")
    public TaskProgressLog getById(@PathVariable Integer id) {
        return progressService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TaskProgressLog> create(@Valid @RequestBody TaskProgressLogRequest request) {
        TaskProgressLog created = progressService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        progressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
