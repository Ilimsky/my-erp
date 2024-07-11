package com.example.employeeservice.controllers;

import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.exceptions.EmployeeNotFoundException;
import com.example.employeeservice.services.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @Operation(summary="Get all employees",
            description = "This method retrieves all employees from database")
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        log.info("Received request to get all employees");
        List<EmployeeDTO> employeeDTOs = service.getAllEmployees();
        log.info("Retrieved employees " + employeeDTOs.size());
        return ResponseEntity.ok(employeeDTOs);
    }

    @Operation(summary = "Get employee by ID",
        description = "This method retrieves an employee by their ID")
    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id){
        log.info("Received request to get employee wwith ID: " + id);
        Optional<EmployeeDTO> employeeDTO = service.getEmployeeById(id);
        if (employeeDTO.isPresent()){
            log.info("Found employee: " + employeeDTO.get());
            return ResponseEntity.ok(employeeDTO.get());
        }else {
            log.warn("Employee wwith id: " + id + " not found");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update employee by ID",
    description = "This method updates an employee by their ID")
    @PutMapping("/employee/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO){
        log.info("Received request to update employee with ID: " + id);
        try {
            EmployeeDTO updatedEmployeeDTO = service.updateEmployee(id, employeeDTO);
            log.info("Updated employee: " + updatedEmployeeDTO);
            return ResponseEntity.ok(updatedEmployeeDTO);
        }catch (EmployeeNotFoundException e){
            log.warn("Employee with id: " + id + " not found");
            return ResponseEntity.notFound().build();
        }
    }


}
