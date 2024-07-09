package com.example.employeeservice.dtos;

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

// Указываем, что данный класс может быть сериализован (переведен в поток байтов для хранения или передачи)
public class EmployeeDTO {

    private Long employeeDTOId;


    private String employeeDTOName;

}

