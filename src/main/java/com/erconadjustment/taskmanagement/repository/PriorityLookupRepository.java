package com.erconadjustment.taskmanagement.repository;

import com.erconadjustment.taskmanagement.entity.PriorityLookup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityLookupRepository extends JpaRepository<PriorityLookup, Integer> {
}
