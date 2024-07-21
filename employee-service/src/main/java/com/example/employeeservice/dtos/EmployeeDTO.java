package com.example.employeeservice.dtos;

import com.example.departmentservice.dtos.DepartmentDTO;
import lombok.*;

// Аннотация Lombok, которая автоматически генерирует геттеры, сеттеры, методы equals, hashCode и toString
@Data
// Аннотация Lombok, которая генерирует конструктор без параметров
@NoArgsConstructor
// Аннотация Lombok, которая генерирует конструктор с параметрами для всех полей класса
@AllArgsConstructor
// Аннотация Lombok, которая генерирует строитель (Builder) для класса
@Builder
@Getter
@Setter
public class EmployeeDTO {

    private Long employeeDTOId;
    private String employeeDTOName;

    private Long departmentDTOId;
    private String departmentDTOName;
    private DepartmentDTO departmentDTO;

    public EmployeeDTO(Long employeeDTOId, String employeeDTOName) {
        this.employeeDTOId = employeeDTOId;
        this.employeeDTOName = employeeDTOName;
    }
}

