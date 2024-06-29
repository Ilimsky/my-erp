package com.example.departmentservice.services;

import com.example.departmentservice.entities.Department;
import com.example.departmentservice.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentRepository repository;



    @Override
    public Department save(Department department) {
        return repository.save(department);
    }

    @Override
    public Department getOne(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public List<Department> getAll() {
        return repository.findAll();
    }

    @Override
    public Department updateOne(Long id, Department department) {
        Department existDepartment = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        if (department.getName() != null) {
            existDepartment.setName(department.getName());
        }
        if (department.getAddress() != null) {
            existDepartment.setAddress(department.getAddress());
        }
        repository.save(existDepartment);
        return existDepartment;
    }

    @Override
    public Department delete(Long id) {
        Department existDepartment = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        repository.deleteById(id);
        return existDepartment;
    }
}
