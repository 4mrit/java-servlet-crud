package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.EmployeeDao;
import com.example.model.Employee;

public class EmployeeServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    List<Employee> employees;

    try {
      employees = EmployeeDao.getAllEmployees();
      PrintWriter out = response.getWriter();
      for (Employee employee : employees) {
        out.println("ID: " + employee.getId()
            + ", Name: " + employee.getName()
            + ", Position: " + employee.getPosition()
            + ", Salary: " + employee.getSalary());
      }
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      PrintWriter out = response.getWriter();
      out.print("Internal server Error !!");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    Employee employee = new Employee();
    String name = request.getParameter("name");
    String position = request.getParameter("position");
    String salaryString = request.getParameter("salary");

    if (name.isEmpty() || position.isEmpty() || salaryString.isEmpty()) {
      PrintWriter out = response.getWriter();
      out.print("All fields are Necessary !!");
      return;
    }

    employee.setName(name);
    employee.setPosition(position);
    employee.setSalary(Double.parseDouble(salaryString));

    try {
      EmployeeDao.addEmployee(employee);
      PrintWriter out = response.getWriter();
      out.print("Employee added Successfully !!");

      RequestDispatcher requestDispatcher = request.getRequestDispatcher("/employees/success");
      requestDispatcher.forward(request, response);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().print("Error adding Employee !!");
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");

    Employee employee = new Employee();
    String idString = request.getParameter("id");
    String name = request.getParameter("name");
    String position = request.getParameter("position");
    String salaryString = request.getParameter("salary");

    if (name.isEmpty() || position.isEmpty() || salaryString.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      PrintWriter out = response.getWriter();
      out.print("All fields are Necessary !!");
      return;
    }

    employee.setId(Integer.parseInt(idString));
    employee.setName(name);
    employee.setPosition(position);
    employee.setSalary(Double.parseDouble(salaryString));

    try {
      EmployeeDao.updateEmployee(employee);
      PrintWriter out = response.getWriter();
      out.print("Employee updated Successfully !!");

      RequestDispatcher requestDispatcher = request.getRequestDispatcher("/employees/success");
      requestDispatcher.include(request, response);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().print("Error updating Employee !!");
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");

    int employeeId = 0;
    employeeId = Integer.parseInt(req.getParameter("id"));

    if (employeeId == 0) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().print("All fields are Necessary !!");
      return;
    }

    try {
      EmployeeDao.deleteEmployee(employeeId);
      PrintWriter out = resp.getWriter();
      out.print("Employee deleted Successfully !!");
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      resp.getWriter().print("Error deleting Employee !!");
    }
  }
}
