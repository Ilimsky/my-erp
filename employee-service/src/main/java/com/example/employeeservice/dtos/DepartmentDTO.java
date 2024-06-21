package com.example.employeeservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// Аннотация Lombok, которая автоматически генерирует геттеры, сеттеры, методы equals, hashCode и toString
@Data
// Аннотация Lombok, которая генерирует конструктор без параметров
@NoArgsConstructor
// Аннотация Lombok, которая генерирует конструктор с параметрами для всех полей класса
@AllArgsConstructor
// Аннотация Lombok, которая генерирует строитель (Builder) для класса
@Builder
// Указываем, что данный класс может быть сериализован (переведен в поток байтов для хранения или передачи)
public class DepartmentDTO implements Serializable {

    // Уникальный идентификатор версии для сериализации
    private static final Long serialVersionUUID = 1L;

    // Поле для хранения идентификатора отдела
    private Long departmentDTOId;

    // Поле для хранения названия отдела
    private String departmentDTOName;

    // Поле для хранения сотрудника (другой DTO-объект)
    // Аннотация Jackson, указывающая включать поле только если оно не равно null
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EmployeeDTO employeeDTO;
}

