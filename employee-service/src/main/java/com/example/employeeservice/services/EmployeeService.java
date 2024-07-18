package com.example.employeeservice.services;

import com.example.employeeservice.dtos.EmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    // Метод для создания нового сотрудника
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);


    // Метод для получения всех сотрудников
    List<EmployeeDTO> getAllEmployees();

    // Метод для получения сотрудника по его идентификатору
    Optional<EmployeeDTO> getEmployeeById(Long id);

    // Метод для обновления существующего сотрудника
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    // Метод для удаления сотрудника по его идентификатору
    void delete(Long id);

}
