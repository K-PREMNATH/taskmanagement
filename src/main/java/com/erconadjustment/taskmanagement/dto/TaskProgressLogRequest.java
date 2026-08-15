package com.erconadjustment.taskmanagement.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TaskProgressLogRequest {
    @NotNull(message = "assignmentId is required")
    private Integer assignmentId;

    @NotNull(message = "statusId is required")
    private Integer statusId;

    @NotNull(message = "percentComplete is required")
    @Min(value = 0, message = "percentComplete must be >= 0")
    @Max(value = 100, message = "percentComplete must be <= 100")
    private Short percentComplete;

    private String remarks;

    @NotNull(message = "updatedBy is required")
    private Integer updatedBy;

    public Integer getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Integer assignmentId) { this.assignmentId = assignmentId; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public Short getPercentComplete() { return percentComplete; }
    public void setPercentComplete(Short percentComplete) { this.percentComplete = percentComplete; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Integer getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Integer updatedBy) { this.updatedBy = updatedBy; }
}
