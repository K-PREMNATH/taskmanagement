package com.erconadjustment.taskmanagement.service;

import com.erconadjustment.taskmanagement.dto.EmployeeRequest;
import com.erconadjustment.taskmanagement.entity.Employee;
import com.erconadjustment.taskmanagement.exception.ResourceNotFoundException;
import com.erconadjustment.taskmanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public Employee create(EmployeeRequest request) {
        Employee employee = new Employee();
        applyRequest(employee, request);
        return employeeRepository.save(employee);
    }

    public Employee update(Integer id, EmployeeRequest request) {
        Employee employee = findById(id);
        applyRequest(employee, request);
        return employeeRepository.save(employee);
    }

    public void delete(Integer id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
    }

    private void applyRequest(Employee employee, EmployeeRequest request) {
        employee.setEmployeeName(request.getEmployeeName());
        employee.setEmail(request.getEmail());
        employee.setRole(request.getRole());
        employee.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
    }
}
