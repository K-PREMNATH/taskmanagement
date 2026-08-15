package com.erconadjustment.taskmanagement.dto;

import javax.validation.constraints.NotBlank;

public class SystemRequest {
    @NotBlank(message = "systemName is required")
    private String systemName;
    private String description;
    private Boolean isActive = true;

    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
