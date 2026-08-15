package com.erconadjustment.taskmanagement.controller;

import com.erconadjustment.taskmanagement.entity.PriorityLookup;
import com.erconadjustment.taskmanagement.entity.TaskStatusLookup;
import com.erconadjustment.taskmanagement.service.LookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lookups")
public class LookupController {

    @Autowired
    private LookupService lookupService;

    @GetMapping("/statuses")
    public List<TaskStatusLookup> getStatuses() {
        return lookupService.findAllStatuses();
    }

    @GetMapping("/priorities")
    public List<PriorityLookup> getPriorities() {
        return lookupService.findAllPriorities();
    }
}
