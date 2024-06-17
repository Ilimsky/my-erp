package com.example.departmentservice.services;

import com.example.departmentservice.entities.Department;

import java.util.List;

public interface DepartmentService {
    Department save(Department department);
    Department getOne(Long id);

    List<Department> getAll();

    Department updateOne(Long id, Department department);

    Department delete(Long id);

}
