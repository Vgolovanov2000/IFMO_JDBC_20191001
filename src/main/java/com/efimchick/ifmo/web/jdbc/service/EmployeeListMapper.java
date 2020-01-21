package com.efimchick.ifmo.web.jdbc.service;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListMapper implements ListMapper<Employee> {
    @Override
    public List<Employee> mapList(ResultSet resultSet) {
        return mapList(resultSet, false);
    }

    public List<Employee> mapList(ResultSet resultSet, boolean fullChain) {
        List<Employee> employees;
        try {
            employees = new ArrayList<>();
            while (resultSet.next()) {
                employees.add(mapRow(resultSet, fullChain));
            }
            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employee mapRow(ResultSet resultSet, boolean fullChain) throws SQLException {
        List<Department> departments = queryDepartments("SELECT * FROM DEPARTMENT WHERE ID = " + resultSet.getString("DEPARTMENT"));
        Department department = (departments != null && !departments.isEmpty()) ? departments.get(0) : null;
        Employee manager;
        if (fullChain) {
            try (Connection connection = ConnectionSource.instance().createConnection();
                 Statement statement = connection.createStatement()) {
                if (resultSet.getString("MANAGER") != null) {
                    ResultSet managerSet = statement.executeQuery("SELECT * FROM EMPLOYEE WHERE ID = " + resultSet.getString("MANAGER"));
                    managerSet.next();
                    manager = mapRow(managerSet, true);
                } else manager = null;
            }
        } else manager = getOnlyOneManager(resultSet);
        return new Employee(
                resultSet.getBigDecimal("ID").toBigInteger(),
                new FullName(
                        resultSet.getString("FIRSTNAME"),
                        resultSet.getString("LASTNAME"),
                        resultSet.getString("MIDDLENAME")
                ),
                Position.valueOf(
                        resultSet.getString("POSITION")),
                resultSet.getDate("HIREDATE").toLocalDate(),
                resultSet.getBigDecimal("SALARY"),
                manager,
                department
        );
    }

    private Employee getOnlyOneManager(ResultSet resultSet1) throws SQLException {
        ResultSet resultSet;
        int managerId = resultSet1.getInt("MANAGER");
        if (managerId != 0) {
            try (Connection connection = ConnectionSource.instance().createConnection();
                 Statement statement = connection.createStatement()) {
                resultSet = statement.executeQuery("SELECT * FROM EMPLOYEE WHERE ID = " + managerId);
            }
            resultSet.next();

            List<Department> departments = queryDepartments("SELECT * FROM DEPARTMENT WHERE ID = " + resultSet.getString("DEPARTMENT"));
            Department department = (departments != null && !departments.isEmpty()) ? departments.get(0) : null;
            return new Employee(
                    (resultSet.getBigDecimal("ID").toBigInteger()),
                    new FullName(
                            resultSet.getString("FIRSTNAME"),
                            resultSet.getString("LASTNAME"),
                            resultSet.getString("MIDDLENAME")
                    ),
                    Position.valueOf(
                            resultSet.getString("POSITION")),
                    resultSet.getDate("HIREDATE").toLocalDate(),
                    resultSet.getBigDecimal("SALARY"),
                    null,
                    department);
        } else return null;
    }

    private List<Department> queryDepartments(String sql) {
        try (Connection connection = ConnectionSource.instance().createConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            return new ListMapperFactory().departmentListMapper().mapList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}