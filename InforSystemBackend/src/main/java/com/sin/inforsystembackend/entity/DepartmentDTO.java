package com.sin.inforsystembackend.entity;

import java.util.List;

public class DepartmentDTO {
    private Integer departmentId;
    private String departmentName;
    private List<String> userRoles;  // 假设你只关心部门内的角色

    // Getters and Setters

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }
}