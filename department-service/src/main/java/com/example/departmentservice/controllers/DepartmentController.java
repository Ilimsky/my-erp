package com.example.departmentservice.controllers;

import com.example.departmentservice.dtos.DepartmentDTO;
import com.example.departmentservice.exceptions.DepartmentNotFoundException;
import com.example.departmentservice.services.DepartmentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Department methods")
@Slf4j
@CrossOrigin(origins = "http://localhost:62949")
public class DepartmentController {

    private final DepartmentServiceImpl service;


    @Autowired
    public DepartmentController(DepartmentServiceImpl service) {
        this.service = service;
    }

    @Operation(summary = "To create new department in the DB",
            description = "This method receives department's DTO and puts this entity into the DB")
    @PostMapping("/department")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        log.info("Received request to create department: " + departmentDTO);
        DepartmentDTO createDepartmentDTO = service.createDept(departmentDTO);
        log.info("Created department: " + createDepartmentDTO);
        return ResponseEntity.ok(createDepartmentDTO);
    }

    @Operation(summary="Get all departments",
            description = "This method retrieves all departments from database")
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(){
        log.info("Received request to get all departments");
        List<DepartmentDTO> departmentDTOs = service.getAllDepts();
        log.info("Retrieved departments " + departmentDTOs.size());
        return ResponseEntity.ok(departmentDTOs);
    }

    @Operation(summary = "Get department by ID",
            description = "This method retrieves an department by their ID")
    @GetMapping("/department/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id){
        log.info("Received request to get department with ID: " + id);
        Optional<DepartmentDTO> departmentDTO = service.getOneDeptById(id);
        if (departmentDTO.isPresent()){
            log.info("Found department: " + departmentDTO.get());
            return ResponseEntity.ok(departmentDTO.get());
        }else {
            log.warn("Department wwith id: " + id + " not found");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update department by ID",
            description = "This method updates an department by their ID")
    @PutMapping("/department/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO){
        log.info("Received request to update department with ID: " + id);
        try {
            DepartmentDTO updatedDepartmentDTO = service.updateDept(id, departmentDTO);
            log.info("Updated department: " + updatedDepartmentDTO);
            return ResponseEntity.ok(updatedDepartmentDTO);
        }catch (DepartmentNotFoundException e){
            log.warn("Department with id: " + id + " not found");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete department by ID", description = "This method deletes an department by their ID")
    @DeleteMapping("/department/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id){
        log.info("Received request to delete department with ID: " + id);
        try {
            service.deleteDept(id);
            log.info("Deleted department with ID: " + id);
            return ResponseEntity.noContent().build();
        }catch (DepartmentNotFoundException e){
            log.warn("Department with ID: " + id + " not found");
            return ResponseEntity.notFound().build();
        }
    }
}
