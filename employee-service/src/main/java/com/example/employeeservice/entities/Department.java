package com.example.employeeservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

// Указываем, что данный класс является сущностью JPA
//@Entity
// Аннотация Lombok, которая автоматически генерирует геттеры, сеттеры, методы equals, hashCode и toString
//@Data
// Аннотация Lombok, которая генерирует конструктор с параметрами для всех полей класса
//@AllArgsConstructor
// Аннотация Lombok, которая генерирует конструктор без параметров
//@NoArgsConstructor
// Аннотация Lombok, которая используется для автоматического создания класса Builder
//@Builder
public class Department {

    // Аннотация JPA, указывающая, что поле является первичным ключом
//    @Id
    // Автоматическая генерация значений для поля id с использованием стратегии IDENTITY
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long departmentId;

    // Поле для хранения названия отдела
//    private String departmentName;

    // Связь один-ко-многим с классом Employee, управление связью осуществляется через поле "department" в классе Employee
//    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // Исключение поля employees из методов equals и hashCode, чтобы избежать потенциальных циклических ссылок
//    @EqualsAndHashCode.Exclude
    // Исключение поля employees из метода toString для предотвращения циклических ссылок
//    @ToString.Exclude
//    private Set<Employee> employees;
}

