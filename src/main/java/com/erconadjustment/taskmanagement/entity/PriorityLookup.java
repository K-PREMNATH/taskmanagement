package com.erconadjustment.taskmanagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "PriorityLookup")
public class PriorityLookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PriorityID")
    private Integer priorityId;

    @Column(name = "PriorityName", nullable = false, unique = true, length = 20)
    private String priorityName;

    public PriorityLookup() {}

    public Integer getPriorityId() { return priorityId; }
    public void setPriorityId(Integer priorityId) { this.priorityId = priorityId; }

    public String getPriorityName() { return priorityName; }
    public void setPriorityName(String priorityName) { this.priorityName = priorityName; }
}
