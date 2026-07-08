package com.example.payroll_system.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "leaves")
@Data
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType; 
    private String status = "PENDING"; 
    private String reason;

    @Column(length = 500)
    private String rejectionReason; // NEW: To store why an admin rejected it
}