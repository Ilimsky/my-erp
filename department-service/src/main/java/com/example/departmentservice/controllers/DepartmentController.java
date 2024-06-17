package com.example.departmentservice.controllers;

import com.example.departmentservice.entities.Department;
import com.example.departmentservice.services.DepartmentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {

    final DepartmentServiceImpl service;


    public DepartmentController(DepartmentServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/department")
    public ResponseEntity<Department> saveDepartment(@RequestBody Department department) {
        Department saveDepartment = service.save(department);
        return new ResponseEntity<>(saveDepartment, HttpStatus.CREATED);
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<Department> getOneDepartment(@PathVariable(name = "id") Long departmentId) {
        Department department = service.getOne(departmentId);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        return service.getAll();
    }

    @PatchMapping("/department/{id}")
    public ResponseEntity<Department> update(@PathVariable(name = "id") Long departmentId, @RequestBody Department department) {
        Department updateDepartment = service.updateOne(departmentId, department);
        return new ResponseEntity<>(updateDepartment, HttpStatus.OK);
    }

    @DeleteMapping("/department/{id}")
    public ResponseEntity<Department> delete(@PathVariable(name = "id") Long departmentId) {
        Department deleteDepartment = service.delete(departmentId);
        return new ResponseEntity<>(deleteDepartment, HttpStatus.OK);
    }
}
