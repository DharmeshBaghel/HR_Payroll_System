package com.example.payroll_system.controller;

import com.example.payroll_system.model.Employee;
import com.example.payroll_system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // NEW: CSV Export Endpoint
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportEmployeesCsv() {
        String csvData = employeeService.generateEmployeeCsv();
        byte[] csvBytes = csvData.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "employee_hr_report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }

    // Secure Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<Employee> login(@RequestBody Map<String, String> credentials) {
        try {
            Long id = Long.parseLong(credentials.get("id"));
            String password = credentials.get("password");
            
            Employee emp = employeeService.authenticate(id, password);
            if (emp != null) {
                return ResponseEntity.ok(emp); // 200 OK
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Error
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}