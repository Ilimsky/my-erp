package com.example.employeeservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

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

// Указываем, что данный класс может быть сериализован (переведен в поток байтов для хранения или передачи)
public class EmployeeDTO implements Serializable {

    // Уникальный идентификатор версии для сериализации
    private static final long serialVersionUUID = 1L;

    private Long employeeDTOId;


    private String employeeDTOName;


    // Поле для хранения отдела (другой DTO-объект)
    // Аннотация Jackson, указывающая имя свойства при сериализации/десериализации в JSON
    @JsonProperty("department")
    // Аннотация Jackson, указывающая включать поле только если оно не равно null
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DepartmentDTO departmentDTO;
}

