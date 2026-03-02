package com.company.leaveManagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class LeaveManagementFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldApplyAndApproveLeave() throws Exception {
        String employeePayload = """
                {
                  \"name\": \"Alice\",
                  \"email\": \"alice@example.com\",
                  \"department\": \"Engineering\",
                  \"leaveBalance\": 10
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeePayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        String leavePayload = """
                {
                  \"employeeId\": 1,
                  \"startDate\": \"2099-01-10\",
                  \"endDate\": \"2099-01-12\",
                  \"reason\": \"Vacation\"
                }
                """;

        mockMvc.perform(post("/api/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(leavePayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));

        String decisionPayload = """
                {
                  \"status\": \"APPROVED\",
                  \"comment\": \"Enjoy\"
                }
                """;

        mockMvc.perform(put("/api/leaves/1/decision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(decisionPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}
