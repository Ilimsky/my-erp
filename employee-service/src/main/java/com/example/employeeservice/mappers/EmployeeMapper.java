package com.example.employeeservice.mappers;

import com.example.employeeservice.dtos.DepartmentDTO;
import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.entities.Department;
import com.example.employeeservice.entities.Employee;

public interface EmployeeMapper {

    /**
     * Преобразует объект Employee в объект EmployeeDTO.
     *
     * @param employee объект Employee, который необходимо преобразовать
     * @return объект EmployeeDTO, соответствующий переданному Employee
     */
    static EmployeeDTO map(final Employee employee) {
        // Используем билдера для создания нового объекта EmployeeDTO
        return EmployeeDTO.builder()
                // Устанавливаем идентификатор EmployeeDTO из Employee
                .employeeDTOId(employee.getEmployeeId())
                // Устанавливаем имя EmployeeDTO из Employee
                .employeeDTOName(employee.getEmployeeName())
                // Создаем объект DepartmentDTO на основе данных из Employee
                .departmentDTO(DepartmentDTO.builder()
                        // Устанавливаем идентификатор DepartmentDTO из Department в Employee
                        .departmentDTOId(employee.getDepartment().getDepartmentId())
                        // Устанавливаем имя DepartmentDTO из Department в Employee
                        .departmentDTOName(employee.getDepartment().getDepartmentName())
                        .build())
                .build();
    }

    /**
     * Преобразует объект типа EmployeeDTO в объект типа Employee.
     *
     * @param employeeDTO объект EmployeeDTO для преобразования
     * @return объект Employee, полученный из объекта EmployeeDTO
     */
    static Employee map(final EmployeeDTO employeeDTO) {
        return Employee.builder()
                .employeeId(employeeDTO.getEmployeeDTOId()) // Устанавливаем идентификатор сотрудника
                .employeeName(employeeDTO.getEmployeeDTOName()) // Устанавливаем имя сотрудника
                .department(Department.builder()
                        .departmentId(employeeDTO.getDepartmentDTO().getDepartmentDTOId()) // Устанавливаем идентификатор отдела
                        .departmentName(employeeDTO.getDepartmentDTO().getDepartmentDTOName()) // Устанавливаем название отдела
                        .build()) // Строим объект отдела
                .build(); // Строим объект сотрудника
    }

}

