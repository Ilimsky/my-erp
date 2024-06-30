package com.example.employeeservice.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
//        // Настройка маппинга DTO -> Entity
//        modelMapper.typeMap(EmployeeDTO.class, Employee.class).addMappings(mapper -> {
//            mapper.map(EmployeeDTO::getEmployeeDTOId, Employee::setEmployeeId);
//            mapper.map(EmployeeDTO::getEmployeeDTOName, Employee::setEmployeeName);
//        });
//
//        // Настройка маппинга Entity -> DTO
//        modelMapper.typeMap(Employee.class, EmployeeDTO.class).addMappings(mapper -> {
//            mapper.map(Employee::getEmployeeId, EmployeeDTO::setEmployeeDTOId);
//            mapper.map(Employee::getEmployeeName, EmployeeDTO::setEmployeeDTOName);
//        });
        return modelMapper();
    }

}
