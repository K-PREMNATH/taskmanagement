package com.erconadjustment.taskmanagement.repository;

import com.erconadjustment.taskmanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
