package com.efimchick.ifmo.web.jdbc.service;

import java.sql.ResultSet;
import java.util.List;

public interface ListMapper <T> {
    List<T> mapList(ResultSet resultSet);
}