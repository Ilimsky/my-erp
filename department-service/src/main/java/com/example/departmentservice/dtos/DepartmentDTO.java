package com.example.departmentservice.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO{
    private Long departmentDTOId;
    private String departmentDTOName;
}
