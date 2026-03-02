package com.company.leaveManagement.controller;

import com.company.leaveManagement.dto.ApplyLeaveRequest;
import com.company.leaveManagement.dto.LeaveDecisionRequest;
import com.company.leaveManagement.entity.LeaveRequest;
import com.company.leaveManagement.service.LeaveRequestService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveRequest apply(@Valid @RequestBody ApplyLeaveRequest request) {
        return leaveRequestService.apply(request);
    }

    @PutMapping("/{id}/decision")
    public LeaveRequest decide(@PathVariable Long id, @Valid @RequestBody LeaveDecisionRequest request) {
        return leaveRequestService.decide(id, request);
    }

    @GetMapping
    public List<LeaveRequest> getAll(@RequestParam(required = false) Long employeeId) {
        if (employeeId != null) {
            return leaveRequestService.getByEmployee(employeeId);
        }
        return leaveRequestService.getAll();
    }
}
