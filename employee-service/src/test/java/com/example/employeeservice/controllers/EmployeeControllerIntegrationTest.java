package com.example.employeeservice.controllers;

import com.example.employeeservice.repositories.EmployeeRepository;
import com.example.employeeservice.services.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private EmployeeServiceImpl service;

    @Test
    void createEmployee_Success() throws Exception {
        String employeeJson = "{\"employeeDTOId\":null,\"employeeDTOName\":\"John Doe\",\"departmentId\":1,\"departmentDTO\":null}";

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeDTOName").value("John Doe"));
    }

    @Test
    void getAllEmployees_Success() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getEmployeeById_NotFound() throws Exception {
        mockMvc.perform(get("/employee/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployee_Success() throws Exception {
        String employeeJson = "{\"employeeDTOId\":1,\"employeeDTOName\":\"Jane Doe\",\"departmentId\":1,\"departmentDTO\":null}";

        mockMvc.perform(put("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeDTOName").value("Jane Doe"));
    }

    @Test
    void deleteEmployee_Success() throws Exception {
        mockMvc.perform(delete("/employee/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmployee_NotFound() throws Exception {
        mockMvc.perform(delete("/employee/999"))
                .andExpect(status().isNotFound());
    }
}
