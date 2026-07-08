package com.example.payroll_system.controller;

import com.example.payroll_system.model.Payroll;
import com.example.payroll_system.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// These three imports are required for downloading files
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    // POST: http://localhost:8080/api/payroll/generate/1?month=July&year=2026
    @PostMapping("/generate/{employeeId}")
    public Payroll generatePayroll(
            @PathVariable Long employeeId,
            @RequestParam String month,
            @RequestParam int year) {
        return payrollService.generatePayroll(employeeId, month, year);
    }

    // GET: http://localhost:8080/api/payroll/employee/1
    @GetMapping("/employee/{employeeId}")
    public List<Payroll> getEmployeePayroll(@PathVariable Long employeeId) {
        return payrollService.getPayrollByEmployeeId(employeeId);
    }

    // NEW: GET request to download the PDF
    @GetMapping("/{payrollId}/download")
    public ResponseEntity<byte[]> downloadPayslip(@PathVariable Long payrollId) {
        byte[] pdfBytes = payrollService.generatePayslipPdf(payrollId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "payslip_" + payrollId + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}