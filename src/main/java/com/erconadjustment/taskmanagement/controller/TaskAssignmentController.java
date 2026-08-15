package com.erconadjustment.taskmanagement.controller;

import com.erconadjustment.taskmanagement.dto.TaskAssignmentRequest;
import com.erconadjustment.taskmanagement.entity.TaskAssignment;
import com.erconadjustment.taskmanagement.service.TaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class TaskAssignmentController {

    @Autowired
    private TaskAssignmentService assignmentService;

    @GetMapping
    public List<TaskAssignment> getAll(@RequestParam(required = false) Integer taskId,
                                        @RequestParam(required = false) Integer employeeId) {
        if (taskId != null) {
            return assignmentService.findByTask(taskId);
        }
        if (employeeId != null) {
            return assignmentService.findByEmployee(employeeId);
        }
        return assignmentService.findAll();
    }

    @GetMapping("/{id}")
    public TaskAssignment getById(@PathVariable Integer id) {
        return assignmentService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TaskAssignment> create(@Valid @RequestBody TaskAssignmentRequest request) {
        TaskAssignment created = assignmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public TaskAssignment update(@PathVariable Integer id, @Valid @RequestBody TaskAssignmentRequest request) {
        return assignmentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        assignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
