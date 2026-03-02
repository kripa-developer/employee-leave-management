package com.company.leaveManagement.service;

import com.company.leaveManagement.dto.CreateEmployeeRequest;
import com.company.leaveManagement.entity.Employee;
import com.company.leaveManagement.exception.BusinessException;
import com.company.leaveManagement.exception.ResourceNotFoundException;
import com.company.leaveManagement.repository.EmployeeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee create(CreateEmployeeRequest request) {
        employeeRepository.findByEmail(request.email()).ifPresent(existing -> {
            throw new BusinessException("Employee already exists with email: " + request.email());
        });

        Employee employee = new Employee();
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setDepartment(request.department());
        employee.setLeaveBalance(request.leaveBalance());
        return employeeRepository.save(employee);
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }
}
