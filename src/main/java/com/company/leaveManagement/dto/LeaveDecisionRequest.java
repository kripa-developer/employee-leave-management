package com.company.leaveManagement.dto;

import com.company.leaveManagement.entity.LeaveStatus;
import jakarta.validation.constraints.NotNull;

public record LeaveDecisionRequest(
        @NotNull LeaveStatus status,
        String comment
) {
}
