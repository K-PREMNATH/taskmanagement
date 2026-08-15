package com.erconadjustment.taskmanagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TaskID")
    private Integer taskId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SystemID", nullable = false)
    private SystemEntity system;

    @Column(name = "TaskName", nullable = false, length = 200)
    private String taskName;

    @Column(name = "Description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PriorityID")
    private PriorityLookup priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StatusID", nullable = false)
    private TaskStatusLookup status;

    @Column(name = "PlannedStartDate")
    private LocalDate plannedStartDate;

    @Column(name = "PlannedEndDate")
    private LocalDate plannedEndDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CreatedBy")
    private Employee createdBy;

    @Column(name = "CreatedDate", insertable = false, updatable = false)
    private LocalDateTime createdDate;

    public Task() {}

    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }

    public SystemEntity getSystem() { return system; }
    public void setSystem(SystemEntity system) { this.system = system; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public PriorityLookup getPriority() { return priority; }
    public void setPriority(PriorityLookup priority) { this.priority = priority; }

    public TaskStatusLookup getStatus() { return status; }
    public void setStatus(TaskStatusLookup status) { this.status = status; }

    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    public void setPlannedStartDate(LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }

    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    public void setPlannedEndDate(LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }

    public Employee getCreatedBy() { return createdBy; }
    public void setCreatedBy(Employee createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
