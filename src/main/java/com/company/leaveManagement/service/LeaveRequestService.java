package com.company.leaveManagement.service;

import com.company.leaveManagement.dto.ApplyLeaveRequest;
import com.company.leaveManagement.dto.LeaveDecisionRequest;
import com.company.leaveManagement.entity.Employee;
import com.company.leaveManagement.entity.LeaveRequest;
import com.company.leaveManagement.entity.LeaveStatus;
import com.company.leaveManagement.exception.BusinessException;
import com.company.leaveManagement.exception.ResourceNotFoundException;
import com.company.leaveManagement.repository.EmployeeRepository;
import com.company.leaveManagement.repository.LeaveRequestRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public LeaveRequest apply(ApplyLeaveRequest request) {
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.employeeId()));

        validateDateRange(request.startDate(), request.endDate());
        long leaveDays = ChronoUnit.DAYS.between(request.startDate(), request.endDate()) + 1;
        if (leaveDays > employee.getLeaveBalance()) {
            throw new BusinessException("Insufficient leave balance. Available: " + employee.getLeaveBalance());
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setStartDate(request.startDate());
        leaveRequest.setEndDate(request.endDate());
        leaveRequest.setReason(request.reason());
        leaveRequest.setStatus(LeaveStatus.PENDING);
        return leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public LeaveRequest decide(Long leaveRequestId, LeaveDecisionRequest request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + leaveRequestId));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessException("Only pending leave requests can be processed");
        }

        if (request.status() == LeaveStatus.PENDING) {
            throw new BusinessException("Decision status must be APPROVED or REJECTED");
        }

        leaveRequest.setStatus(request.status());
        leaveRequest.setDecidedOn(LocalDate.now());
        leaveRequest.setDecisionComment(request.comment());

        if (request.status() == LeaveStatus.APPROVED) {
            long leaveDays = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;
            Employee employee = leaveRequest.getEmployee();
            if (leaveDays > employee.getLeaveBalance()) {
                throw new BusinessException("Insufficient leave balance at approval stage");
            }
            employee.setLeaveBalance((int) (employee.getLeaveBalance() - leaveDays));
            employeeRepository.save(employee);
        }

        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getAll() {
        return leaveRequestRepository.findAll();
    }

    public List<LeaveRequest> getByEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BusinessException("End date cannot be before start date");
        }
    }
}
