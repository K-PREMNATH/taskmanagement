package com.erconadjustment.taskmanagement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class TaskRequest {
    @NotNull(message = "systemId is required")
    private Integer systemId;

    @NotBlank(message = "taskName is required")
    private String taskName;

    private String description;
    private Integer priorityId;

    @NotNull(message = "statusId is required")
    private Integer statusId;

    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private Integer createdBy;

    public Integer getSystemId() { return systemId; }
    public void setSystemId(Integer systemId) { this.systemId = systemId; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getPriorityId() { return priorityId; }
    public void setPriorityId(Integer priorityId) { this.priorityId = priorityId; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    public void setPlannedStartDate(LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }
    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    public void setPlannedEndDate(LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }
}
