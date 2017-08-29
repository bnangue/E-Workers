package com.bricenangue.insyconn.e_workers.model;

/**
 * Created by bricenangue on 30.08.17.
 */

public class EmployeePosition {
    private String companyName, departmentName, position;

    public EmployeePosition() {
    }

    public EmployeePosition(String companyName, String departmentName, String position) {
        this.companyName=companyName;
        this.departmentName=departmentName;
        this.position=position;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
