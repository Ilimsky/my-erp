package com.example.employeeservice.services;

import com.example.departmentservice.dtos.DepartmentDTO;
import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.entities.Employee;
import com.example.employeeservice.exceptions.EmployeeNotFoundException;
import com.example.employeeservice.feign.DepartmentClient;
import com.example.employeeservice.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository repository;
    private final ModelMapper modelMapper;
    private final DepartmentClient departmentClient;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository, ModelMapper modelMapper, DepartmentClient departmentClient) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.departmentClient = departmentClient;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        logger.debug("Received EmployeeDTO for creation: {}", employeeDTO);

        try {
            // Маппинг DTO в сущность
            Employee employee = modelMapper.map(employeeDTO, Employee.class);
            logger.debug("Mapped Employee entity before saving: {}", employee);

            // Сохранение сотрудника
            employee = repository.save(employee);
            logger.debug("Saved Employee entity: {}", employee);

            // Маппинг обратно в DTO
            EmployeeDTO result = modelMapper.map(employee, EmployeeDTO.class);
            logger.debug("Mapped EmployeeDTO after saving: {}", result);

            // Получение данных о департаменте
            if (employee.getDepartmentId() != null) {
                try {
                    DepartmentDTO departmentDTO = departmentClient.getDepartmentById(employee.getDepartmentId());
                    logger.debug("Retrieved DepartmentDTO: {}", departmentDTO);

                    // Установка данных о департаменте
                    result.setDepartmentDTO(departmentDTO);

                    // Обновление поля DEPARTMENT_NAME в базе данных
                    employee.setDepartmentName(departmentDTO.getDepartmentDTOName());
                    repository.save(employee); // Сохраняем обновленный объект с DEPARTMENT_NAME

                } catch (Exception ex) {
                    logger.error("Failed to retrieve DepartmentDTO for departmentId {}: {}", employee.getDepartmentId(), ex.getMessage(), ex);
                    result.setDepartmentDTO(null); // Установка null для департамента при ошибке
                }
            } else {
                logger.debug("DepartmentId is null for employee with ID: {}", employee.getEmployeeId());
            }

            return result;
        } catch (Exception ex) {
            logger.error("Failed to create employee: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to create employee", ex);
        }
    }


    @Override
    public List<EmployeeDTO> getAllEmployees() {
        logger.debug("Fetching all employees");

        try {
            List<Employee> employees = repository.findAll();
            logger.debug("Retrieved Employee list from repository: {}", employees);

            List<EmployeeDTO> employeeDTOs = employees.stream()
                    .map(e -> {
                        EmployeeDTO employeeDTO = modelMapper.map(e, EmployeeDTO.class);
                        logger.debug("Mapped EmployeeDTO for employee: {} -> {}", e, employeeDTO);

                        if (e.getDepartmentId() != null) {
                            try {
                                DepartmentDTO departmentDTO = departmentClient.getDepartmentById(e.getDepartmentId());
                                logger.debug("Retrieved DepartmentDTO for departmentId {}: {}", e.getDepartmentId(), departmentDTO);
                                employeeDTO.setDepartmentDTO(departmentDTO);
                            } catch (Exception ex) {
                                logger.error("Failed to retrieve DepartmentDTO for departmentId {}: {}", e.getDepartmentId(), ex.getMessage());
                                employeeDTO.setDepartmentDTO(null); // Optionally handle this scenario
                            }
                        } else {
                            logger.debug("DepartmentId is null for employee with ID: {}", e.getEmployeeId());
                        }

                        return employeeDTO;
                    })
                    .collect(Collectors.toList());

            logger.debug("Final list of EmployeeDTOs: {}", employeeDTOs);

            return employeeDTOs;
        } catch (Exception ex) {
            logger.error("Failed to fetch employees: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch employees", ex); // Or handle in a way appropriate for your application
        }
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        logger.debug("Fetching employee with id: {}", id);

        try {
            Optional<Employee> employeeOptional = repository.findById(id);
            if (employeeOptional.isEmpty()) {
                logger.debug("Employee with id {} not found", id);
                throw new EmployeeNotFoundException("Employee with id " + id + " not found");
            }

            EmployeeDTO employeeDTO = modelMapper.map(employeeOptional.get(), EmployeeDTO.class);
            logger.debug("Mapped EmployeeDTO for employee with id {}: {}", id, employeeDTO);

            if (employeeOptional.get().getDepartmentId() != null) {
                try {
                    DepartmentDTO departmentDTO = departmentClient.getDepartmentById(employeeOptional.get().getDepartmentId());
                    logger.debug("Retrieved DepartmentDTO for departmentId {}: {}", employeeOptional.get().getDepartmentId(), departmentDTO);
                    employeeDTO.setDepartmentDTO(departmentDTO);
                } catch (Exception ex) {
                    logger.error("Failed to retrieve DepartmentDTO for departmentId {}: {}", employeeOptional.get().getDepartmentId(), ex.getMessage());
                    employeeDTO.setDepartmentDTO(null); // Optionally handle this scenario
                }
            } else {
                logger.debug("DepartmentId is null for employee with ID: {}", id);
            }

            return Optional.of(employeeDTO);
        } catch (Exception ex) {
            logger.error("Failed to fetch employee with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch employee", ex); // Or handle in a way appropriate for your application
        }
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        logger.debug("Updating employee with id: {} using EmployeeDTO: {}", id, employeeDTO);

        try {
            Optional<Employee> employeeOptional = repository.findById(id);
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                logger.debug("Found employee for update: {}", employee);

                // Обновляем поля сотрудника
                employee.setEmployeeName(employeeDTO.getEmployeeDTOName());
                employee.setDepartmentId(employeeDTO.getDepartmentId());
                employee = repository.save(employee);

                // Маппинг обновленного сотрудника в DTO
                EmployeeDTO updatedEmployeeDTO = modelMapper.map(employee, EmployeeDTO.class);
                logger.debug("Updated EmployeeDTO: {}", updatedEmployeeDTO);

                // Получение данных о департаменте
                if (employee.getDepartmentId() != null) {
                    try {
                        DepartmentDTO departmentDTO = departmentClient.getDepartmentById(employee.getDepartmentId());
                        logger.debug("Retrieved DepartmentDTO for departmentId {}: {}", employee.getDepartmentId(), departmentDTO);
                        updatedEmployeeDTO.setDepartmentDTO(departmentDTO);
                    } catch (Exception ex) {
                        logger.error("Failed to retrieve DepartmentDTO for departmentId {}: {}", employee.getDepartmentId(), ex.getMessage(), ex);
                        updatedEmployeeDTO.setDepartmentDTO(null); // Установка null для департамента при ошибке
                    }
                } else {
                    logger.debug("DepartmentId is null for employee with ID: {}", id);
                }

                return updatedEmployeeDTO;
            } else {
                logger.debug("Employee with id {} not found", id);
                throw new EmployeeNotFoundException("Employee with id " + id + " not found");
            }
        } catch (Exception ex) {
            logger.error("Failed to update employee with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Failed to update employee", ex);
        }
    }


    @Override
    public void delete(Long id) {
        logger.debug("Deleting employee with id: {}", id);

        try {
            if (!repository.existsById(id)) {
                logger.debug("Employee with id {} not found", id);
                throw new EmployeeNotFoundException("Employee with id " + id + " not found");
            }

            repository.deleteById(id);
            logger.debug("Deleted employee with id: {}", id);
        } catch (Exception ex) {
            logger.error("Failed to delete employee with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Failed to delete employee", ex); // Or handle in a way appropriate for your application
        }
    }
}
