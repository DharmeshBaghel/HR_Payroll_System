package com.example.payroll_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    // @GeneratedValue is removed so we can assign a custom 6-digit ID manually
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // NEW: Password Field

    private String department;
    
    private String role;
    
    private Double baseSalary;

    // Leave Balances
    private int sickLeaveBalance = 12;
    private int casualLeaveBalance = 15;
}