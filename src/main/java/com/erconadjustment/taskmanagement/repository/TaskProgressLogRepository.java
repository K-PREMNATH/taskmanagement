package com.erconadjustment.taskmanagement.repository;

import com.erconadjustment.taskmanagement.entity.TaskProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskProgressLogRepository extends JpaRepository<TaskProgressLog, Integer> {
    List<TaskProgressLog> findByAssignment_AssignmentIdOrderByUpdateDateDesc(Integer assignmentId);
}
