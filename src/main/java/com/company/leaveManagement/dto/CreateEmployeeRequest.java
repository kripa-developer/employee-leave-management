package com.company.leaveManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateEmployeeRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String department,
        @Min(0) int leaveBalance
) {
}
