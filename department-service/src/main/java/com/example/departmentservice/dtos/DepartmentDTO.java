package com.example.departmentservice.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private String address;
}
