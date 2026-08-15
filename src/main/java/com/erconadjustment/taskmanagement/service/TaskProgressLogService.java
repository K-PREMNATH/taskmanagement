package com.erconadjustment.taskmanagement.service;

import com.erconadjustment.taskmanagement.dto.TaskProgressLogRequest;
import com.erconadjustment.taskmanagement.entity.*;
import com.erconadjustment.taskmanagement.exception.ResourceNotFoundException;
import com.erconadjustment.taskmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskProgressLogService {

    @Autowired
    private TaskProgressLogRepository progressRepository;
    @Autowired
    private TaskAssignmentRepository assignmentRepository;
    @Autowired
    private TaskStatusLookupRepository statusRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<TaskProgressLog> findAll() {
        return progressRepository.findAll();
    }

    public List<TaskProgressLog> findByAssignment(Integer assignmentId) {
        return progressRepository.findByAssignment_AssignmentIdOrderByUpdateDateDesc(assignmentId);
    }

    public TaskProgressLog findById(Integer id) {
        return progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress entry not found with id: " + id));
    }

    /**
     * Logging progress also syncs the parent assignment's current status,
     * so the assignment always reflects the most recent log entry.
     */
    public TaskProgressLog create(TaskProgressLogRequest request) {
        TaskAssignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + request.getAssignmentId()));
        TaskStatusLookup status = statusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found with id: " + request.getStatusId()));
        Employee updatedBy = employeeRepository.findById(request.getUpdatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getUpdatedBy()));

        TaskProgressLog log = new TaskProgressLog();
        log.setAssignment(assignment);
        log.setStatus(status);
        log.setPercentComplete(request.getPercentComplete());
        log.setRemarks(request.getRemarks());
        log.setUpdatedBy(updatedBy);
        TaskProgressLog saved = progressRepository.save(log);

        assignment.setStatus(status);
        assignmentRepository.save(assignment);

        return saved;
    }

    public void delete(Integer id) {
        TaskProgressLog log = findById(id);
        progressRepository.delete(log);
    }
}
