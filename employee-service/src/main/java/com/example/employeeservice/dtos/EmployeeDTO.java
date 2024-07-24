package com.example.employeeservice.dtos;

import com.example.departmentservice.dtos.DepartmentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long employeeDTOId;
    private String employeeDTOName;
    private Long departmentId;
    private DepartmentDTO departmentDTO;
}