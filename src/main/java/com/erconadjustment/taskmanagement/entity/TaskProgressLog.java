package com.erconadjustment.taskmanagement.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TaskProgressLog")
public class TaskProgressLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProgressID")
    private Integer progressId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AssignmentID", nullable = false)
    private TaskAssignment assignment;

    @Column(name = "UpdateDate", insertable = false, updatable = false)
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StatusID", nullable = false)
    private TaskStatusLookup status;

    @Column(name = "PercentComplete", nullable = false)
    private Short percentComplete;

    @Column(name = "Remarks", length = 500)
    private String remarks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UpdatedBy", nullable = false)
    private Employee updatedBy;

    public TaskProgressLog() {}

    public Integer getProgressId() { return progressId; }
    public void setProgressId(Integer progressId) { this.progressId = progressId; }

    public TaskAssignment getAssignment() { return assignment; }
    public void setAssignment(TaskAssignment assignment) { this.assignment = assignment; }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }

    public TaskStatusLookup getStatus() { return status; }
    public void setStatus(TaskStatusLookup status) { this.status = status; }

    public Short getPercentComplete() { return percentComplete; }
    public void setPercentComplete(Short percentComplete) { this.percentComplete = percentComplete; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Employee getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Employee updatedBy) { this.updatedBy = updatedBy; }
}
