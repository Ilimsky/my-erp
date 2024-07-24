package com.example.employeeservice.services;

import com.example.employeeservice.dtos.EmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    List<EmployeeDTO> getAllEmployees();

    Optional<EmployeeDTO> getEmployeeById(Long id);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    void delete(Long id);
}
