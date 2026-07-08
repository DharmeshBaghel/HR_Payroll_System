package com.example.payroll_system.service;

import com.example.payroll_system.model.Employee;
import com.example.payroll_system.model.Payroll;
import com.example.payroll_system.model.Leave;
import com.example.payroll_system.repository.EmployeeRepository;
import com.example.payroll_system.repository.PayrollRepository;
import com.example.payroll_system.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    // Logic to calculate and generate a payslip
    public Payroll generatePayroll(Long employeeId, String month, int year) {
        // 1. Find the employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // 2. Smart Payroll: Check unpaid leaves
        List<Leave> leaves = leaveRepository.findByEmployeeId(employeeId);
        long unpaidDays = leaves.stream()
            .filter(l -> "APPROVED".equalsIgnoreCase(l.getStatus()) && "UNPAID".equalsIgnoreCase(l.getLeaveType()))
            .mapToLong(l -> java.time.temporal.ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
            .sum();

        Double dailyRate = employee.getBaseSalary() / 30;
        Double unpaidDeduction = unpaidDays * dailyRate;

        // 3. Perform Salary Calculations
        Double basic = employee.getBaseSalary() - unpaidDeduction;
        Double tax = basic * 0.10; // 10% Flat Tax Deduction
        Double net = basic - tax;

        // 4. Create the Payroll Record
        Payroll payroll = new Payroll();
        payroll.setEmployee(employee);
        payroll.setSalaryMonth(month);
        payroll.setSalaryYear(year);
        payroll.setBasicSalary(employee.getBaseSalary());
        payroll.setTaxDeduction(tax + unpaidDeduction); // Combine deductions for simplicity
        payroll.setNetSalary(net);

        // 5. Save to Database
        return payrollRepository.save(payroll);
    }

    // Fetch payslips for an employee
    public List<Payroll> getPayrollByEmployeeId(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId);
    }

    // NEW: Generate PDF Payslip (This is the method your Controller was looking for!)
    public byte[] generatePayslipPdf(Long payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
            .orElseThrow(() -> new RuntimeException("Payroll not found"));
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        
        document.open();
        document.add(new Paragraph("=================================="));
        document.add(new Paragraph("          OFFICIAL PAYSLIP        "));
        document.add(new Paragraph("=================================="));
        document.add(new Paragraph("Employee Name: " + payroll.getEmployee().getName()));
        document.add(new Paragraph("Department: " + payroll.getEmployee().getDepartment()));
        document.add(new Paragraph("Month/Year: " + payroll.getSalaryMonth() + " " + payroll.getSalaryYear()));
        document.add(new Paragraph("----------------------------------"));
        document.add(new Paragraph("Base Salary: Rs. " + payroll.getBasicSalary()));
        document.add(new Paragraph("Total Deductions: Rs. " + payroll.getTaxDeduction()));
        document.add(new Paragraph("----------------------------------"));
        document.add(new Paragraph("NET PAY: Rs. " + payroll.getNetSalary()));
        document.add(new Paragraph("=================================="));
        document.close();
        
        return out.toByteArray();
    }
}