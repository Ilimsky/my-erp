package com.example.employeeservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

// Указываем, что данный класс является сущностью JPA
@Entity
// Аннотация Lombok, которая генерирует конструктор с параметрами для всех полей класса
@AllArgsConstructor
// Аннотация Lombok, которая генерирует конструктор без параметров
@NoArgsConstructor
// Аннотация Lombok, которая используется для автоматического создания класса Builder
@Builder
@Getter
@Setter
public class Employee {

    // Аннотация JPA, указывающая, что поле является первичным ключом
    @Id
    // Автоматическая генерация значений для поля id с использованием стратегии IDENTITY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    // Поле для хранения имени сотрудника
    private String employeeName;

    private Long departmentId;
    private String departmentName;

}

