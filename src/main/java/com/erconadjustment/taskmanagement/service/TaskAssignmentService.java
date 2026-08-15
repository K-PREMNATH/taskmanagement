package com.erconadjustment.taskmanagement.service;

import com.erconadjustment.taskmanagement.dto.TaskAssignmentRequest;
import com.erconadjustment.taskmanagement.entity.*;
import com.erconadjustment.taskmanagement.exception.ResourceNotFoundException;
import com.erconadjustment.taskmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssignmentService {

    @Autowired
    private TaskAssignmentRepository assignmentRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TaskStatusLookupRepository statusRepository;

    public List<TaskAssignment> findAll() {
        return assignmentRepository.findAll();
    }

    public List<TaskAssignment> findByTask(Integer taskId) {
        return assignmentRepository.findByTask_TaskId(taskId);
    }

    public List<TaskAssignment> findByEmployee(Integer employeeId) {
        return assignmentRepository.findByEmployee_EmployeeId(employeeId);
    }

    public TaskAssignment findById(Integer id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
    }

    public TaskAssignment create(TaskAssignmentRequest request) {
        validateDateRange(request);
        TaskAssignment assignment = new TaskAssignment();
        applyRequest(assignment, request);
        return assignmentRepository.save(assignment);
    }

    public TaskAssignment update(Integer id, TaskAssignmentRequest request) {
        validateDateRange(request);
        TaskAssignment assignment = findById(id);
        applyRequest(assignment, request);
        return assignmentRepository.save(assignment);
    }

    public void delete(Integer id) {
        TaskAssignment assignment = findById(id);
        assignmentRepository.delete(assignment);
    }

    private void validateDateRange(TaskAssignmentRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("endDate cannot be before startDate");
        }
    }

    private void applyRequest(TaskAssignment assignment, TaskAssignmentRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));
        TaskStatusLookup status = statusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found with id: " + request.getStatusId()));

        assignment.setTask(task);
        assignment.setEmployee(employee);
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        assignment.setManDaysAllocated(request.getManDaysAllocated());
        assignment.setManDaysActual(request.getManDaysActual());
        assignment.setStatus(status);
    }
}
