package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class DaoFactory {

    private static final ConnectionSource CONNECTION_SOURCE = ConnectionSource.instance();
    private static final ListMapperFactory LIST_MAPPER_FACTORY = new ListMapperFactory();

    private List<Employee> queryEmployees(String sql) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            return LIST_MAPPER_FACTORY.employeeListMapper().mapList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int update(String sql) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private List<Department> queryDepartments(String sql) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            return LIST_MAPPER_FACTORY.departmentListMapper().mapList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EmployeeDao employeeDAO() {
        return new EmployeeDao() {
            @Override
            public List<Employee> getByDepartment(Department department) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " + department.getId();
                return queryEmployees(sqlQuery);
            }

            @Override
            public List<Employee> getByManager(Employee employee) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE MANAGER = " + employee.getId();
                return queryEmployees(sqlQuery);
            }

            @Override
            public Optional<Employee> getById(BigInteger Id) {
                String sqlQuery = "SELECT * FROM EMPLOYEE WHERE ID = " + Id;
                List<Employee> employees = queryEmployees(sqlQuery);
                assert employees != null;
                return employees.isEmpty() ?
                        Optional.empty() :
                        Optional.ofNullable(employees.get(0));
            }

            @Override
            public List<Employee> getAll() {
                String sqlQuery = "SELECT * FROM EMPLOYEE";
                return queryEmployees(sqlQuery);
            }

            @Override
            public Employee save(Employee employee) {
                delete(employee);
                update(
                        "INSERT INTO EMPLOYEE VALUES (" +
                                employee.getId() + ", '" +
                                employee.getFullName().getFirstName() + "', '" +
                                employee.getFullName().getLastName() + "', '" +
                                employee.getFullName().getMiddleName() + "', '" +
                                employee.getPosition() + "', " +
                                employee.getManagerId() + ", '" +
                                employee.getHired() + "', " +
                                employee.getSalary().toBigInteger() + ", " +
                                employee.getDepartmentId() + ")");
                return employee;
            }

            @Override
            public void delete(Employee employee) {
                if (getById(employee.getId()).isPresent()) {
                    update("DELETE FROM EMPLOYEE WHERE ID = " + employee.getId());
                }
            }
        };
    }

    public DepartmentDao departmentDAO() {
        return new DepartmentDao() {
            @Override
            public Optional<Department> getById(BigInteger Id) {
                String sqlQuery = "SELECT * FROM DEPARTMENT WHERE ID = " + Id;
                List<Department> departments = queryDepartments(sqlQuery);
                assert departments != null;
                return departments.isEmpty() ?
                        Optional.empty() :
                        Optional.ofNullable(departments.get(0));
            }

            @Override
            public List<Department> getAll() {
                String sqlQuery = "SELECT * FROM DEPARTMENT";
                return queryDepartments(sqlQuery);
            }

            @Override
            public Department save(Department department) {
                delete(department);
                update("INSERT INTO DEPARTMENT VALUES ("+department.getId()+", '"+department.getName()+"', '"+department.getLocation()+"')");
                return department;
            }

            @Override
            public void delete(Department department) {
                if (getById(department.getId()).isPresent()) {
                    update("DELETE FROM DEPARTMENT WHERE ID = " + department.getId());
                }
            }
        };
    }
}
