package com.erconadjustment.taskmanagement.repository;

import com.erconadjustment.taskmanagement.entity.TaskStatusLookup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusLookupRepository extends JpaRepository<TaskStatusLookup, Integer> {
}
