package com.erconadjustment.taskmanagement.controller;

import com.erconadjustment.taskmanagement.dto.SystemRequest;
import com.erconadjustment.taskmanagement.entity.SystemEntity;
import com.erconadjustment.taskmanagement.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/systems")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @GetMapping
    public List<SystemEntity> getAll() {
        return systemService.findAll();
    }

    @GetMapping("/{id}")
    public SystemEntity getById(@PathVariable Integer id) {
        return systemService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SystemEntity> create(@Valid @RequestBody SystemRequest request) {
        SystemEntity created = systemService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public SystemEntity update(@PathVariable Integer id, @Valid @RequestBody SystemRequest request) {
        return systemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        systemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
