package com.efimchick.ifmo.web.jdbc;

import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


import com.efimchick.ifmo.web.jdbc.domain.Employee;

public class SetMapperFactory {

    public SetMapper<Set<Employee>> employeesSetMapper() {
        return resultSet -> {
            Set<Employee> employees = null;
            try {
                employees = new HashSet<>();
                while (resultSet.next()) {
                    employees.add(mapRow(resultSet, resultSet.getRow()));
                }
            } catch (SQLException ignored) {
            }
            return employees;
        };
    }

    private Employee mapRow(ResultSet resultSet, int position) throws SQLException {
        Employee manager = null;
        int IdOfManager = resultSet.getInt("MANAGER");
        if (resultSet.getString("MANAGER") != null) {
            resultSet.absolute(0);
            while (resultSet.next()) {
                if (Integer.parseInt(resultSet.getString("ID")) == IdOfManager)
                    manager = mapRow(resultSet, resultSet.getRow());
            }
            resultSet.absolute(position);
        }
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
                manager);
    }
    }
