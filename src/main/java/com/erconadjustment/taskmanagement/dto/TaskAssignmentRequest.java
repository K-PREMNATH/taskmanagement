package com.erconadjustment.taskmanagement.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TaskAssignmentRequest {
    @NotNull(message = "taskId is required")
    private Integer taskId;

    @NotNull(message = "employeeId is required")
    private Integer employeeId;

    @NotNull(message = "startDate is required")
    private LocalDate startDate;

    @NotNull(message = "endDate is required")
    private LocalDate endDate;

    @NotNull(message = "manDaysAllocated is required")
    private BigDecimal manDaysAllocated;

    private BigDecimal manDaysActual;

    @NotNull(message = "statusId is required")
    private Integer statusId;

    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BigDecimal getManDaysAllocated() { return manDaysAllocated; }
    public void setManDaysAllocated(BigDecimal manDaysAllocated) { this.manDaysAllocated = manDaysAllocated; }
    public BigDecimal getManDaysActual() { return manDaysActual; }
    public void setManDaysActual(BigDecimal manDaysActual) { this.manDaysActual = manDaysActual; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
}
