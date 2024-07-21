package com.example.employeeservice.dtos;

import com.example.departmentservice.dtos.DepartmentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Аннотация Lombok, которая автоматически генерирует геттеры, сеттеры, методы equals, hashCode и toString
@Data
// Аннотация Lombok, которая генерирует конструктор без параметров
@NoArgsConstructor
// Аннотация Lombok, которая генерирует конструктор с параметрами для всех полей класса
@AllArgsConstructor
// Аннотация Lombok, которая генерирует строитель (Builder) для класса
@Builder
public class EmployeeDTO {

    private Long employeeDTOId;
    private String employeeDTOName;

    private Long departmentId; // Поле для идентификатора отдела
    private DepartmentDTO departmentDTO; // Поле для DTO отдела
}

