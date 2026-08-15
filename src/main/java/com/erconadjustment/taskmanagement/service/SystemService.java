package com.erconadjustment.taskmanagement.service;

import com.erconadjustment.taskmanagement.dto.SystemRequest;
import com.erconadjustment.taskmanagement.entity.SystemEntity;
import com.erconadjustment.taskmanagement.exception.ResourceNotFoundException;
import com.erconadjustment.taskmanagement.repository.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemService {

    @Autowired
    private SystemRepository systemRepository;

    public List<SystemEntity> findAll() {
        return systemRepository.findAll();
    }

    public SystemEntity findById(Integer id) {
        return systemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("System not found with id: " + id));
    }

    public SystemEntity create(SystemRequest request) {
        SystemEntity system = new SystemEntity();
        applyRequest(system, request);
        return systemRepository.save(system);
    }

    public SystemEntity update(Integer id, SystemRequest request) {
        SystemEntity system = findById(id);
        applyRequest(system, request);
        return systemRepository.save(system);
    }

    public void delete(Integer id) {
        SystemEntity system = findById(id);
        systemRepository.delete(system);
    }

    private void applyRequest(SystemEntity system, SystemRequest request) {
        system.setSystemName(request.getSystemName());
        system.setDescription(request.getDescription());
        system.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
    }
}
