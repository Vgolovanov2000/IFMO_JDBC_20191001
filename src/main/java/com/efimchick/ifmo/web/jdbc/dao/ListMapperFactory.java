package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;

public class ListMapperFactory {
    public ListMapper<Employee> employeeListMapper() {
        return new EmployeeListMapper();
    }

    public ListMapper<Department> departmentListMapper() {
        return new DepartmentListMapper();
    }

}
