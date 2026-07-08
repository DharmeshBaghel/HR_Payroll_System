package com.example.payroll_system.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payroll")
@Data
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link the payroll to a specific employee
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private String salaryMonth; // e.g., "July"
    private int salaryYear;     // e.g., 2026

    private Double basicSalary;
    private Double taxDeduction; // Let's say a flat 10% tax for this project
    private Double netSalary;    // basicSalary - taxDeduction
}