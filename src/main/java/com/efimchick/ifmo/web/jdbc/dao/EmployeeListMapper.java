package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListMapper implements ListMapper<Employee> {
    @Override
    public List<Employee> mapList(ResultSet resultSet) {
        List<Employee> employees;
        try {
            employees = new ArrayList<>();
            while (resultSet.next()) {
                employees.add(mapRow(resultSet, resultSet.getRow()));
            }
            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Employee mapRow(ResultSet resultSet, int position) throws SQLException {
        BigInteger manager = resultSet.getObject("MANAGER") != null ?
                resultSet.getBigDecimal("MANAGER").toBigInteger() :
                BigInteger.valueOf(0);
        BigInteger department = resultSet.getObject("DEPARTMENT") != null ?
                resultSet.getBigDecimal("DEPARTMENT").toBigInteger() :
                BigInteger.valueOf(0);
        return new Employee(
                resultSet.getBigDecimal("ID").toBigInteger(),
                new FullName(
                        resultSet.getString("FIRSTNAME"),
                        resultSet.getString("LASTNAME"),
                        resultSet.getString("MIDDLENAME")
                ),
                Position.valueOf(resultSet.getString("POSITION")),
                resultSet.getDate("HIREDATE").toLocalDate(),
                resultSet.getBigDecimal("SALARY"),
                manager,
                department);
    }
}
