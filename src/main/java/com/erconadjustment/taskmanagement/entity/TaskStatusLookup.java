package com.erconadjustment.taskmanagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "TaskStatusLookup")
public class TaskStatusLookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusID")
    private Integer statusId;

    @Column(name = "StatusName", nullable = false, unique = true, length = 30)
    private String statusName;

    public TaskStatusLookup() {}

    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
}
