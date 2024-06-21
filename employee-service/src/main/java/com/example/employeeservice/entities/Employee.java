package com.example.employeeservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Указываем, что данный класс является сущностью JPA
@Entity
// Аннотация Lombok, которая автоматически генерирует геттеры, сеттеры, методы equals, hashCode и toString
@Data
// Аннотация Lombok, которая генерирует конструктор с параметрами для всех полей класса
@AllArgsConstructor
// Аннотация Lombok, которая генерирует конструктор без параметров
@NoArgsConstructor
// Аннотация Lombok, которая используется для автоматического создания класса Builder
@Builder
public class Employee {

    // Аннотация JPA, указывающая, что поле является первичным ключом
    @Id
    // Автоматическая генерация значений для поля id с использованием стратегии IDENTITY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    // Поле для хранения имени сотрудника
    private String employeeName;

    // Связь многие-к-одному с классом Department, данные будут загружаться жадно
    @ManyToOne(fetch = FetchType.EAGER)
    // Настройка колонки для связи с классом Department
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}

