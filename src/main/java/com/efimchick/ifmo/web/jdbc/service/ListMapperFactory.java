package com.efimchick.ifmo.web.jdbc.service;

public class ListMapperFactory {
    public EmployeeListMapper employeeListMapper() {
        return new EmployeeListMapper();
    }

    public DepartmentListMapper departmentListMapper() {
        return new DepartmentListMapper();
    }
}
