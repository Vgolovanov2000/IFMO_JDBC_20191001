package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.domain.Department;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentListMapper implements ListMapper<Department> {
    @Override
    public List<Department> mapList(ResultSet resultSet) {
        List<Department> departments;
        try {
            departments = new ArrayList<>();
            while (resultSet.next()) {
                departments.add(mapRow(resultSet, resultSet.getRow()));
            }
            return departments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Department mapRow(ResultSet resultSet, int position) throws SQLException {
        return new Department(
                resultSet.getBigDecimal("ID").toBigInteger(),
                resultSet.getString("NAME"),
                resultSet.getString("LOCATION")
        );
    }
}
