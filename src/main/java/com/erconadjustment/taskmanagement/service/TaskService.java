package com.erconadjustment.taskmanagement.service;

import com.erconadjustment.taskmanagement.dto.TaskRequest;
import com.erconadjustment.taskmanagement.entity.*;
import com.erconadjustment.taskmanagement.exception.ResourceNotFoundException;
import com.erconadjustment.taskmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private SystemRepository systemRepository;
    @Autowired
    private PriorityLookupRepository priorityRepository;
    @Autowired
    private TaskStatusLookupRepository statusRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findBySystem(Integer systemId) {
        return taskRepository.findBySystem_SystemId(systemId);
    }

    public Task findById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public Task create(TaskRequest request) {
        Task task = new Task();
        applyRequest(task, request);
        return taskRepository.save(task);
    }

    public Task update(Integer id, TaskRequest request) {
        Task task = findById(id);
        applyRequest(task, request);
        return taskRepository.save(task);
    }

    public void delete(Integer id) {
        Task task = findById(id);
        taskRepository.delete(task);
    }

    private void applyRequest(Task task, TaskRequest request) {
        SystemEntity system = systemRepository.findById(request.getSystemId())
                .orElseThrow(() -> new ResourceNotFoundException("System not found with id: " + request.getSystemId()));
        TaskStatusLookup status = statusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found with id: " + request.getStatusId()));

        task.setSystem(system);
        task.setTaskName(request.getTaskName());
        task.setDescription(request.getDescription());
        task.setStatus(status);
        task.setPlannedStartDate(request.getPlannedStartDate());
        task.setPlannedEndDate(request.getPlannedEndDate());

        if (request.getPriorityId() != null) {
            PriorityLookup priority = priorityRepository.findById(request.getPriorityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Priority not found with id: " + request.getPriorityId()));
            task.setPriority(priority);
        } else {
            task.setPriority(null);
        }

        if (request.getCreatedBy() != null) {
            Employee creator = employeeRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getCreatedBy()));
            task.setCreatedBy(creator);
        }
    }
}
