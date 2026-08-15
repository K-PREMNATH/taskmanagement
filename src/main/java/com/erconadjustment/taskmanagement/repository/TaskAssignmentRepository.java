package com.erconadjustment.taskmanagement.repository;

import com.erconadjustment.taskmanagement.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {
    List<TaskAssignment> findByTask_TaskId(Integer taskId);
    List<TaskAssignment> findByEmployee_EmployeeId(Integer employeeId);
}
