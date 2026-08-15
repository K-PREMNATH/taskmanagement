package com.erconadjustment.taskmanagement.service;

import com.erconadjustment.taskmanagement.entity.PriorityLookup;
import com.erconadjustment.taskmanagement.entity.TaskStatusLookup;
import com.erconadjustment.taskmanagement.repository.PriorityLookupRepository;
import com.erconadjustment.taskmanagement.repository.TaskStatusLookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Serves the small, mostly-static Status and Priority lookup lists used to populate frontend dropdowns. */
@Service
public class LookupService {

    @Autowired
    private TaskStatusLookupRepository statusRepository;

    @Autowired
    private PriorityLookupRepository priorityRepository;

    public List<TaskStatusLookup> findAllStatuses() {
        return statusRepository.findAll();
    }

    public List<PriorityLookup> findAllPriorities() {
        return priorityRepository.findAll();
    }
}
