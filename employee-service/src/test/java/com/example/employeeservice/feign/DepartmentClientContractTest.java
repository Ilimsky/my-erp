package com.example.employeeservice.feign;

import com.example.departmentservice.dtos.DepartmentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith({SpringExtension.class, StubRunnerExtension.class})
//@StubRunnerProperties(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:department-service:+:stubs:8081")
public class DepartmentClientContractTest {

    @Autowired
    private DepartmentClient departmentClient;

    private DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() {
        departmentDTO = DepartmentDTO.builder()
                .departmentDTOId(1L)
                .departmentDTOName("HR")
                .build();
    }

    @Test
    void getDepartmentById_Success() {
        DepartmentDTO result = departmentClient.getDepartmentById(1L);

        assertNotNull(result);
        assertEquals(departmentDTO.getDepartmentDTOId(), result.getDepartmentDTOId());
        assertEquals(departmentDTO.getDepartmentDTOName(), result.getDepartmentDTOName());
    }
}
