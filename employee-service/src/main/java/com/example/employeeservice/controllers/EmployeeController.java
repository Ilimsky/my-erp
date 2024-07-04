package com.example.employeeservice.controllers;

import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.services.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/")
public class EmployeeController {

    private final EmployeeServiceImpl service;


    @Autowired
    public EmployeeController(EmployeeServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/employee")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createEmployeeDTO = service.createEmployee(employeeDTO);
        return ResponseEntity.ok(createEmployeeDTO);
    }

}
