package com.example.dao;

import com.example.controller.EmployeeServlet;
import com.example.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

  private static final String URL = "jdbc:postgresql://localhost:5555/mydatabase";
  private static final String USER = "postgres";
  private static final String PASSWORD = "Strong@Passw0rd";

  private static Connection getDbConnection() throws SQLException, ClassNotFoundException {
    Class.forName("org.postgresql.Driver");
    Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
    System.out.println("Connection Sucessfull!");
    return connection;
  }

  public static void addEmployee(Employee employee) {
    try {
      Connection connection = getDbConnection();
      PreparedStatement statement = connection.prepareStatement("INSERT INTO employees"
          + "( Name ,Position, Salary )"
          + "VALUES"
          + "( ?, ?, ?);");

      statement.setString(1, employee.getName());
      statement.setString(2, employee.getPosition());
      statement.setDouble(3, employee.getSalary());

      int rowsAffected = statement.executeUpdate();
      System.out.println("Inserted " + rowsAffected + " rows successfully !!");
      connection.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public static List<Employee> getAllEmployees() {
    List<Employee> employeeList = new ArrayList<>();
    try {
      Connection connection = getDbConnection();

      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM employees");

      while (resultSet.next()) {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("id"));
        employee.setName(resultSet.getString("name"));
        employee.setPosition(resultSet.getString("position"));
        employee.setSalary(resultSet.getDouble("salary"));
        employeeList.add(employee);
      }

      connection.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    return employeeList;
  }

  public static Employee getEmployee(int employeeId) {
    Employee employee = new Employee();
    try {
      Connection connection = getDbConnection();
      PreparedStatement statement = connection.prepareStatement("SELECT FROM employees WHERE Id = ?;");

      statement.setInt(1, employeeId);

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        employee.setId(resultSet.getInt("id"));
        employee.setName(resultSet.getString("name"));
        employee.setPosition(resultSet.getString("position"));
        employee.setSalary(resultSet.getDouble("salary"));
        connection.close();
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return employee;
  }

  public static void updateEmployee(Employee employee) {
    try {
      Connection connection = getDbConnection();
      PreparedStatement statement = connection.prepareStatement("UPDATE employees"
          + " SET name=?, position=?, salary=?"
          + " WHERE id=? ;");

      statement.setString(1, employee.getName());
      statement.setString(2, employee.getPosition());
      statement.setDouble(3, employee.getSalary());
      statement.setInt(4, employee.getId());

      int rowsAffected = statement.executeUpdate();
      System.out.println("Updated" + rowsAffected + " rows successfully !!");
      connection.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public static void deleteEmployee(int employeeId) {
    try {
      Connection connection = getDbConnection();
      PreparedStatement statement = connection.prepareStatement("DELETE FROM employees"
          + " WHERE id=? ;");

      statement.setInt(1, employeeId);

      int rowsAffected = statement.executeUpdate();
      System.out.println("Deleted" + rowsAffected + " rows successfully !!");
      connection.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

}
