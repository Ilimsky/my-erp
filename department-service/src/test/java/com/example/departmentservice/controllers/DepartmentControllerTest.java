package com.example.departmentservice.controllers;

import com.example.departmentservice.dtos.DepartmentDTO;
import com.example.departmentservice.exceptions.DepartmentNotFoundException;
import com.example.departmentservice.services.DepartmentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentServiceImpl service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateDepartment() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentDTOName("16");

        DepartmentDTO createDepartmentDTO = new DepartmentDTO();
        createDepartmentDTO.setDepartmentDTOName("16");

        when(service.createDept(any(DepartmentDTO.class))).thenReturn(createDepartmentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.departmentDTOName").value("16"));
    }

    @Test
    public void testGetAllDepartments() throws Exception {
        DepartmentDTO departmentDTO1 = new DepartmentDTO();
        departmentDTO1.setDepartmentDTOId(1L);
        departmentDTO1.setDepartmentDTOName("16");

        DepartmentDTO departmentDTO2 = new DepartmentDTO();
        departmentDTO2.setDepartmentDTOId(2L);
        departmentDTO2.setDepartmentDTOName("20");

        List<DepartmentDTO> departmentDTOList = Arrays.asList(departmentDTO1, departmentDTO2);

        when(service.getAllDepts()).thenReturn(departmentDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/departments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].departmentDTOId").value(1L))
                .andExpect(jsonPath("$[0].departmentDTOName").value("16"))
                .andExpect(jsonPath("$[1].departmentDTOId").value(2L))
                .andExpect(jsonPath("$[1].departmentDTOName").value("20"));
    }

    @Test
    public void testGetDepartmentById() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentDTOId(1L);
        departmentDTO.setDepartmentDTOName("16");

        when(service.getOneDeptById(anyLong())).thenReturn(Optional.of(departmentDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.departmentDTOId").value(1L))
                .andExpect(jsonPath("$.departmentDTOName").value("16"));
    }

    @Test
    public void testGetDepartmentByIdNotFound() throws Exception {
        when(service.getOneDeptById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentDTOName("Updated Name");

        DepartmentDTO updatedDepartmentDTO = new DepartmentDTO();
        updatedDepartmentDTO.setDepartmentDTOId(1L);
        updatedDepartmentDTO.setDepartmentDTOName("Updated Name");

        when(service.updateDept(anyLong(), any(DepartmentDTO.class))).thenReturn(updatedDepartmentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/department/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.departmentDTOId").value(1L))
                .andExpect(jsonPath("$.departmentDTOName").value("Updated Name"));
    }

    @Test
    public void testUpdateDepartmentNotFound() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentDTOName("Updated Name");

        when(service.updateDept(anyLong(), any(DepartmentDTO.class))).thenThrow(new DepartmentNotFoundException("Department not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/department/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        Long departmentId = 1L;

        willDoNothing().given(service).deleteDept(departmentId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/department/{id}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteDepartmentNotFound() throws Exception {
        Long departmentId = 1L;

        willThrow(new DepartmentNotFoundException("Department not found")).given(service).deleteDept(departmentId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/department/{id}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}