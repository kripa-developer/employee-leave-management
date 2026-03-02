package com.company.leaveManagement.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ApplyLeaveRequest(
        @NotNull Long employeeId,
        @NotNull @FutureOrPresent LocalDate startDate,
        @NotNull @FutureOrPresent LocalDate endDate,
        @NotBlank String reason
) {
}
