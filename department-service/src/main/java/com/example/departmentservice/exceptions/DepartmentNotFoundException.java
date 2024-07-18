package com.example.departmentservice.exceptions;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String message) {
        // Передаем сообщение об ошибке в конструктор родительского класса
        super(message);
    }
}
