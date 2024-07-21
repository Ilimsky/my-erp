package com.example.employeeservice.controllers;


import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.exceptions.EmployeeNotFoundException;
import com.example.employeeservice.services.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServiceImpl service;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeDTOName("John Doe");

        EmployeeDTO createEmployeeDTO = new EmployeeDTO();
        createEmployeeDTO.setEmployeeDTOName("John Doe");

        when(service.createEmployee(any(EmployeeDTO.class))).thenReturn(createEmployeeDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeDTOName").value("John Doe"));
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.setEmployeeDTOId(1L);
        employeeDTO1.setEmployeeDTOName("John Doe");

        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setEmployeeDTOId(2L);
        employeeDTO2.setEmployeeDTOName("Jane Smith");

        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO1, employeeDTO2);

        when(service.getAllEmployees()).thenReturn(employeeDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].employeeDTOId").value(1L))
                .andExpect(jsonPath("$[0].employeeDTOName").value("John Doe"))
                .andExpect(jsonPath("$[1].employeeDTOId").value(2L))
                .andExpect(jsonPath("$[1].employeeDTOName").value("Jane Smith"));
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeDTOId(1L);
        employeeDTO.setEmployeeDTOName("John Doe");

        when(service.getEmployeeById(anyLong())).thenReturn(Optional.of(employeeDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeDTOId").value(1L))
                .andExpect(jsonPath("$.employeeDTOName").value("John Doe"));
    }

    @Test
    public void testGetEmployeeByIdNotFound() throws Exception {
        when(service.getEmployeeById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeDTOName("Updated Name");

        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setEmployeeDTOId(1L);
        updatedEmployeeDTO.setEmployeeDTOName("Updated Name");

        when(service.updateEmployee(anyLong(), any(EmployeeDTO.class))).thenReturn(updatedEmployeeDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/employee/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeDTOId").value(1L))
                .andExpect(jsonPath("$.employeeDTOName").value("Updated Name"));
    }

    @Test
    public void testUpdateEmployeeNotFound() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeDTOName("Updated Name");

        when(service.updateEmployee(anyLong(), any(EmployeeDTO.class))).thenThrow(new EmployeeNotFoundException("Employee not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/employee/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Long employeeId = 1L;

        willDoNothing().given(service).delete(employeeId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteEmployeeNotFound() throws Exception {
        Long employeeId = 1L;

        willThrow(new EmployeeNotFoundException("Employee not found")).given(service).delete(employeeId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}