package com.example.employeeservice.controllers;

import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.services.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Employee methods")
@Slf4j
public class EmployeeController {

    private final EmployeeServiceImpl service;


    @Autowired
    public EmployeeController(EmployeeServiceImpl service) {
        this.service = service;
    }

    @Operation(summary = "To create new employee in the DB",
    description = "This method receives employee's DTO and puts this entity into the DB")
    @PostMapping("/employee")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Received request to create employee: " + employeeDTO);
        EmployeeDTO createEmployeeDTO = service.createEmployee(employeeDTO);
        log.info("Created employee: " + createEmployeeDTO);
        return ResponseEntity.ok(createEmployeeDTO);
    }

}
