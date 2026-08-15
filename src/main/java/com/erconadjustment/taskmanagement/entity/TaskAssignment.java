package com.erconadjustment.taskmanagement.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TaskAssignments")
public class TaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AssignmentID")
    private Integer assignmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TaskID", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employee;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "ManDaysAllocated", nullable = false, precision = 5, scale = 2)
    private BigDecimal manDaysAllocated;

    @Column(name = "ManDaysActual", precision = 5, scale = 2)
    private BigDecimal manDaysActual;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StatusID", nullable = false)
    private TaskStatusLookup status;

    @Column(name = "AssignedDate", insertable = false, updatable = false)
    private LocalDateTime assignedDate;

    public TaskAssignment() {}

    public Integer getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Integer assignmentId) { this.assignmentId = assignmentId; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getManDaysAllocated() { return manDaysAllocated; }
    public void setManDaysAllocated(BigDecimal manDaysAllocated) { this.manDaysAllocated = manDaysAllocated; }

    public BigDecimal getManDaysActual() { return manDaysActual; }
    public void setManDaysActual(BigDecimal manDaysActual) { this.manDaysActual = manDaysActual; }

    public TaskStatusLookup getStatus() { return status; }
    public void setStatus(TaskStatusLookup status) { this.status = status; }

    public LocalDateTime getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDateTime assignedDate) { this.assignedDate = assignedDate; }
}
