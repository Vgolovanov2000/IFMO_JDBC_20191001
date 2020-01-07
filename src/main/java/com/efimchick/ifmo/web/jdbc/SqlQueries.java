package com.efimchick.ifmo.web.jdbc;

/**
 * Implement sql queries like described
 */
public class SqlQueries {
    //Select all employees sorted by last name in ascending order
    //language=HSQLDB
    String select01 ="SELECT * FROM EMPLOYEE ORDER BY LASTNAME ASC";

    //Select employees having no more than 5 characters in last name sorted by last name in ascending order
    //language=HSQLDB
    String select02 = "SELECT * FROM EMPLOYEE WHERE CHAR_LENGTH( LASTNAME ) <= 5 ORDER BY LASTNAME ASC ";

    //Select employees having salary no less than 2000 and no more than 3000
    //language=HSQLDB
    String select03 = "SELECT * FROM EMPLOYEE WHERE SALARY >= 2000 AND SALARY <= 3000";

    //Select employees having salary no more than 2000 or no less than 3000
    //language=HSQLDB
    String select04 = "SELECT * FROM EMPLOYEE WHERE SALARY <= 2000 OR SALARY >= 3000";

    //Select employees assigned to a department and corresponding department name
    //language=HSQLDB
    String select05 = "SELECT * FROM EMPLOYEE, DEPARTMENT where EMPLOYEE.DEPARTMENT = DEPARTMENT.ID";

    //Select all employees and corresponding department name if there is one.
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select06 ="SELECT T1.*, T2.NAME AS depname FROM EMPLOYEE T1 LEFT JOIN DEPARTMENT T2 ON T1.DEPARTMENT = T2.ID" ;

    //Select total salary pf all employees. Name it "total".
    //language=HSQLDB
    String select07 = "SELECT SUM(SALARY) AS total FROM EMPLOYEE";

    //Select all departments and amount of employees assigned per department
    //Name column containing name of the department "depname".
    //Name column containing employee amount "staff_size".
    //language=HSQLDB
    String select08 = "SELECT DEPARTMENT.NAME AS depname, COUNT(EMPLOYEE.FIRSTNAME) AS staff_size FROM EMPLOYEE, DEPARTMENT WHERE EMPLOYEE.department = DEPARTMENT.ID GROUP BY DEPARTMENT.NAME";

    //Select all departments and values of total and average salary per department
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select09 = "SELECT DEPARTMENT.NAME AS depname, sum(EMPLOYEE.SALARY) AS total, avg(EMPLOYEE.SALARY) AS average FROM EMPLOYEE, DEPARTMENT WHERE EMPLOYEE.DEPARTMENT = DEPARTMENT.ID group by DEPARTMENT.NAME";

    //Select all employees and their managers if there is one.
    //Name column containing employee lastname "employee".
    //Name column containing manager lastname "manager".
    //language=HSQLDB
    String select10 = "SELECT EMPLOYEE.LASTNAME AS EMPLOYEE, MANAGER.LASTNAME AS manager FROM EMPLOYEE LEFT JOIN EMPLOYEE MANAGER ON EMPLOYEE.MANAGER = MANAGER.ID";

}
