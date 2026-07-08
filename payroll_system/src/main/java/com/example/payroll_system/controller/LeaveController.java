package com.example.payroll_system.controller;

import com.example.payroll_system.model.Leave;
import com.example.payroll_system.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    // POST: http://localhost:8080/api/leaves/request
    @PostMapping("/request")
    public Leave requestLeave(@RequestBody Leave leave) {
        return leaveService.requestLeave(leave);
    }

    // GET: http://localhost:8080/api/leaves/employee/1
    @GetMapping("/employee/{employeeId}")
    public List<Leave> getEmployeeLeaves(@PathVariable Long employeeId) {
        return leaveService.getLeavesByEmployeeId(employeeId);
    }

    // NEW: Get all leaves for Admin Dashboard
    @GetMapping("/all")
    public List<Leave> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

    // PUT: http://localhost:8080/api/leaves/1/status?status=APPROVED
    @PutMapping("/{leaveId}/status")
    public Leave updateStatus(
            @PathVariable Long leaveId, 
            @RequestParam String status,
            @RequestParam(required = false) String reason) { // NEW: Accepts optional reason
        return leaveService.updateLeaveStatus(leaveId, status, reason);
    }
}