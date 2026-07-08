package com.example.payroll_system.repository;

import com.example.payroll_system.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    
    // Custom method to fetch all payslips for a specific employee
    List<Payroll> findByEmployeeId(Long employeeId);
}