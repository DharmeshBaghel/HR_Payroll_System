package com.example.payroll_system.service;

import com.example.payroll_system.model.Employee;
import com.example.payroll_system.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee addEmployee(Employee employee) {
        Random random = new Random();
        long newId;
        
        // Generate a random 6-digit number (100000 to 999999)
        do {
            newId = 100000 + random.nextInt(900000);
        } while (employeeRepository.existsById(newId));
        
        employee.setId(newId);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // NEW: Generate CSV Data for the Admin Report
    public String generateEmployeeCsv() {
        List<Employee> employees = employeeRepository.findAll();
        StringBuilder csv = new StringBuilder("Employee ID,Name,Email,Department,Role,Base Salary,Sick Leave Balance,Casual Leave Balance\n");
        
        for (Employee e : employees) {
            csv.append(e.getId()).append(",")
               .append(e.getName()).append(",")
               .append(e.getEmail()).append(",")
               .append(e.getDepartment()).append(",")
               .append(e.getRole()).append(",")
               .append(e.getBaseSalary()).append(",")
               .append(e.getSickLeaveBalance()).append(",")
               .append(e.getCasualLeaveBalance()).append("\n");
        }
        return csv.toString();
    }

    // True Backend Authentication
    public Employee authenticate(Long id, String password) {
        Employee emp = employeeRepository.findById(id).orElse(null);
        if (emp != null && emp.getPassword().equals(password)) {
            return emp; // Password matches!
        }
        return null; // Login failed
    }
}