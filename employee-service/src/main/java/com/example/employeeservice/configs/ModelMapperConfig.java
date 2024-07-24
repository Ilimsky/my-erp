package com.example.employeeservice.configs;

import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.entities.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        // Маппинг EmployeeDTO -> Employee
        modelMapper.typeMap(EmployeeDTO.class, Employee.class).addMappings(mapper -> {
            mapper.map(EmployeeDTO::getEmployeeDTOId, Employee::setEmployeeId);
            mapper.map(EmployeeDTO::getEmployeeDTOName, Employee::setEmployeeName);
            mapper.map(EmployeeDTO::getDepartmentId, Employee::setDepartmentId);
        });

        // Маппинг Employee -> EmployeeDTO
        modelMapper.typeMap(Employee.class, EmployeeDTO.class).addMappings(mapper -> {
            mapper.map(Employee::getEmployeeId, EmployeeDTO::setEmployeeDTOId);
            mapper.map(Employee::getEmployeeName, EmployeeDTO::setEmployeeDTOName);
            mapper.map(Employee::getDepartmentId, EmployeeDTO::setDepartmentId);
        });

        return modelMapper;
    }


}
