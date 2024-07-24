package com.example.employeeservice.feign;

import com.example.departmentservice.dtos.DepartmentDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DepartmentClientTest {

    @Mock
    private DepartmentClient departmentClient;

    @InjectMocks
    private DepartmentClientTest departmentClientTest;

    @Test
    public void testGetDepartmentById(){
        MockitoAnnotations.openMocks(this);

        Long testId = 5L;
        DepartmentDTO mockDepartmentDTO = new DepartmentDTO();
        mockDepartmentDTO.setDepartmentDTOId(testId);

        when(departmentClient.getDepartmentById(testId)).thenReturn(mockDepartmentDTO);

        DepartmentDTO departmentDTO = departmentClient.getDepartmentById(testId);
        System.out.println("DepartmentDTO: " + departmentDTO);
        assertNotNull(departmentDTO);
        assertEquals(testId, departmentDTO.getDepartmentDTOId());
    }
}
