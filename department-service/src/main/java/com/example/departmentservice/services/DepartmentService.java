package com.example.departmentservice.services;

import com.example.departmentservice.dtos.DepartmentDTO;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    DepartmentDTO createDept(DepartmentDTO departmentDTO);

    List<DepartmentDTO> getAllDepts();

    Optional<DepartmentDTO> getOneDeptById(Long id);

    DepartmentDTO updateDept(Long id, DepartmentDTO departmentDTO);

    void deleteDept(Long id);

}
