package com.example.employeeservice.feign;

import com.example.departmentservice.dtos.DepartmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "department-service", url = "http://localhost:8081")
public interface DepartmentClient {
    @GetMapping("/department/{id}")
    DepartmentDTO getDepartmentById(@PathVariable("id") Long id);
}
