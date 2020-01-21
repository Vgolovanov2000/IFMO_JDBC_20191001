package com.efimchick.ifmo.web.jdbc.service;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ServiceFactory {

    private static final ConnectionSource CONNECTION_SOURCE = ConnectionSource.instance();
    private static final ListMapperFactory LIST_MAPPER_FACTORY = new ListMapperFactory();

    private List<Employee> queryEmployees(String sql) {
        return queryEmployees(sql, false);
    }

    private List<Employee> queryEmployees(String sql, boolean fmc) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet resultSet = statement.executeQuery(sql);
            return LIST_MAPPER_FACTORY.employeeListMapper().mapList(resultSet, fmc);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EmployeeService employeeService() {
        return new EmployeeService() {
            @Override
            public List<Employee> getAllSortByHireDate(Paging paging) {
                String sqlQuery =
                        "SELECT * FROM EMPLOYEE ORDER BY HIREDATE";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            private List<Employee> pageOf(List<Employee> employees, Paging paging) {
                return employees.subList(
                        (paging.page - 1) * paging.itemPerPage,
                        Math.min(paging.page * paging.itemPerPage, employees.size()));
            }

            @Override
            public List<Employee> getAllSortByLastname(Paging paging) {
                String sqlQuery = "SELECT * FROM EMPLOYEE ORDER BY LASTNAME";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            @Override
            public List<Employee> getAllSortBySalary(Paging paging) {
                String sqlQuery = "SELECT * FROM EMPLOYEE ORDER BY SALARY";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            @Override
            public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
                String sqlQuery = "SELECT * FROM EMPLOYEE ORDER BY DEPARTMENT, LASTNAME";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " + department.getId() + " ORDER BY HIREDATE";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            @Override
            public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " + department.getId() + " ORDER BY SALARY";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " + department.getId() + " ORDER BY LASTNAME";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            @Override
            public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE MANAGER = " + manager.getId() + " ORDER BY LASTNAME";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            @Override
            public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE MANAGER = " + manager.getId() + " ORDER BY HIREDATE";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            @Override
            public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE MANAGER = " + manager.getId() + " ORDER BY SALARY";
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return pageOf(employees, paging);
            }

            @Override
            public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE ID = " + employee.getId();
                List<Employee> employees = queryEmployees(sqlQuery, true);
                assert employees != null;
                return employees.get(0);
            }

            @Override
            public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " + department.getId() + " ORDER BY SALARY DESC LIMIT 1 OFFSET " + (salaryRank - 1);
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return employees.get(0);
            }
        };
    }
}
