package com.example.payroll_system.service;

import com.example.payroll_system.model.Leave;
import com.example.payroll_system.model.Employee;
import com.example.payroll_system.repository.LeaveRepository;
import com.example.payroll_system.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;

import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Leave requestLeave(Leave leave) {
        long days = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        
        Employee emp = employeeRepository.findById(leave.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if ("SICK".equalsIgnoreCase(leave.getLeaveType()) && days > emp.getSickLeaveBalance()) {
            throw new RuntimeException("Insufficient Sick Leave Balance!");
        } else if ("CASUAL".equalsIgnoreCase(leave.getLeaveType()) && days > emp.getCasualLeaveBalance()) {
            throw new RuntimeException("Insufficient Casual Leave Balance!");
        }

        leave.setStatus("PENDING"); 
        return leaveRepository.save(leave);
    }

    public List<Leave> getLeavesByEmployeeId(Long employeeId) {
        return leaveRepository.findByEmployeeId(employeeId);
    }

    // NEW: Add this missing method for the Admin Dashboard!
    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    // NEW: Added rejectionReason parameter to the signature
    public Leave updateLeaveStatus(Long leaveId, String status, String rejectionReason) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave record not found"));
        
        if ("APPROVED".equalsIgnoreCase(status) && "PENDING".equalsIgnoreCase(leave.getStatus())) {
            Employee emp = leave.getEmployee();
            long days = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
            
            if ("SICK".equalsIgnoreCase(leave.getLeaveType())) {
                emp.setSickLeaveBalance(emp.getSickLeaveBalance() - (int) days);
            } else if ("CASUAL".equalsIgnoreCase(leave.getLeaveType())) {
                emp.setCasualLeaveBalance(emp.getCasualLeaveBalance() - (int) days);
            }
            employeeRepository.save(emp);
        } else if ("REJECTED".equalsIgnoreCase(status) && rejectionReason != null) {
            // NEW: Save the rejection reason to the database
            leave.setRejectionReason(rejectionReason);
        }

        leave.setStatus(status.toUpperCase());
        return leaveRepository.save(leave);
    }
}