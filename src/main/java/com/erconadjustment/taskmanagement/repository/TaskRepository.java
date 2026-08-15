package com.erconadjustment.taskmanagement.repository;

import com.erconadjustment.taskmanagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findBySystem_SystemId(Integer systemId);
    List<Task> findByStatus_StatusId(Integer statusId);
}
