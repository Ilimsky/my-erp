package com.example.employeeservice.exceptions;

// Определяем пользовательское исключение, которое наследует от RuntimeException
public class EmployeeNotFoundException extends RuntimeException {
    // Конструктор, принимающий сообщение об ошибке
    public EmployeeNotFoundException(String message) {
        // Передаем сообщение об ошибке в конструктор родительского класса
        super(message);
    }
}
