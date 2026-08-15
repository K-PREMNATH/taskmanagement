package com.erconadjustment.taskmanagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "Systems")
public class SystemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SystemID")
    private Integer systemId;

    @Column(name = "SystemName", nullable = false, unique = true, length = 100)
    private String systemName;

    @Column(name = "Description", length = 500)
    private String description;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = true;

    public SystemEntity() {}

    public Integer getSystemId() { return systemId; }
    public void setSystemId(Integer systemId) { this.systemId = systemId; }

    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
