package com.example.payroll_system.repository;

import com.example.payroll_system.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // By extending JpaRepository, you instantly get methods like:
    // save(), findAll(), findById(), and deleteById() for free!
    
    // You can also write custom search methods just by naming them correctly:
    Employee findByEmail(String email);
}