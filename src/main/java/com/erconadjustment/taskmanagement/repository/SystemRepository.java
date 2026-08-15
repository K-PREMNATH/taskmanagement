package com.erconadjustment.taskmanagement.repository;

import com.erconadjustment.taskmanagement.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRepository extends JpaRepository<SystemEntity, Integer> {
}
