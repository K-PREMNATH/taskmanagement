package com.erconadjustment.taskmanagement.dto;

import javax.validation.constraints.NotBlank;

public class EmployeeRequest {
    @NotBlank(message = "employeeName is required")
    private String employeeName;
    private String email;
    private String role;
    private Boolean isActive = true;

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
