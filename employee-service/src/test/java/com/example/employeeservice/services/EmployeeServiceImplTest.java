package com.example.employeeservice.services;

import com.example.departmentservice.dtos.DepartmentDTO;
import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.entities.Employee;
import com.example.employeeservice.exceptions.EmployeeNotFoundException;
import com.example.employeeservice.feign.DepartmentClient;
import com.example.employeeservice.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private DepartmentClient departmentClient;

    @InjectMocks
    private EmployeeServiceImpl service;

    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(service, "modelMapper", modelMapper);

        // Настройка маппинга DTO -> Entity
        modelMapper.typeMap(EmployeeDTO.class, Employee.class).addMappings(mapper -> {
            mapper.map(EmployeeDTO::getEmployeeDTOId, Employee::setEmployeeId);
            mapper.map(EmployeeDTO::getEmployeeDTOName, Employee::setEmployeeName);
            mapper.map(EmployeeDTO::getDepartmentId, Employee::setDepartmentId);
        });

        // Настройка маппинга Entity -> DTO
        modelMapper.typeMap(Employee.class, EmployeeDTO.class).addMappings(mapper -> {
            mapper.map(Employee::getEmployeeId, EmployeeDTO::setEmployeeDTOId);
            mapper.map(Employee::getEmployeeName, EmployeeDTO::setEmployeeDTOName);
            mapper.map(Employee::getDepartmentId, EmployeeDTO::setDepartmentId);
        });
    }

    @Test
    void createEmployee() {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("John Doe");

        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "John Doe", 1L, null);

        given(repository.save(any(Employee.class))).willReturn(employee);

        EmployeeDTO createdEmployee = service.createEmployee(employeeDTO);

        assertNotNull(createdEmployee, "The createdEmployee should not be null");
        assertNotNull(createdEmployee.getEmployeeDTOId(), "The EmployeeDTOId should not be null");
        assertNotNull(createdEmployee.getEmployeeDTOName(), "The EmployeeDTOName should not be null");

        assertEquals(1L, createdEmployee.getEmployeeDTOId(), "EmployeeDTOId should be 1");
        assertEquals("John Doe", createdEmployee.getEmployeeDTOName(), "EmployeeDTOName should be 'John Doe'");
    }

    @Test
    void getAllEmployees() {
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1L);
        employee1.setEmployeeName("John Doe");
        employee1.setDepartmentId(1L);

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2L);
        employee2.setEmployeeName("Jane Smith");
        employee2.setDepartmentId(2L);

        List<Employee> employees = Arrays.asList(employee1, employee2);

        DepartmentDTO departmentDTO1 = new DepartmentDTO(1L, "HR");
        DepartmentDTO departmentDTO2 = new DepartmentDTO(2L, "IT");

        given(repository.findAll()).willReturn(employees);
        given(departmentClient.getDepartmentById(1L)).willReturn(departmentDTO1);
        given(departmentClient.getDepartmentById(2L)).willReturn(departmentDTO2);

        List<EmployeeDTO> employeeDTOs = service.getAllEmployees();

        assertNotNull(employeeDTOs, "The employeeDTOs should not be null");
        assertEquals(2, employeeDTOs.size(), "The size of employeeDTOs should be 2");

        EmployeeDTO employeeDTO1 = employeeDTOs.get(0);
        EmployeeDTO employeeDTO2 = employeeDTOs.get(1);

        assertEquals(1L, employeeDTO1.getEmployeeDTOId(), "The first employee's ID should be 1");
        assertEquals("John Doe", employeeDTO1.getEmployeeDTOName(), "The first employee's name should be 'John Doe'");
        assertEquals("HR", employeeDTO1.getDepartmentDTO().getDepartmentDTOName(), "The first employee's department should be 'HR'");

        assertEquals(2L, employeeDTO2.getEmployeeDTOId(), "The second employee's ID should be 2");
        assertEquals("Jane Smith", employeeDTO2.getEmployeeDTOName(), "The second employee's name should be 'Jane Smith'");
        assertEquals("IT", employeeDTO2.getDepartmentDTO().getDepartmentDTOName(), "The second employee's department should be 'IT'");
    }

    @Test
    void getEmployeeById() {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("John Doe");
        employee.setDepartmentId(1L);

        DepartmentDTO departmentDTO = new DepartmentDTO(1L, "HR");

        given(repository.findById(1L)).willReturn(Optional.of(employee));
        given(departmentClient.getDepartmentById(1L)).willReturn(departmentDTO);

        Optional<EmployeeDTO> employeeDTO = service.getEmployeeById(1L);

        assertNotNull(employeeDTO, "The employeeDTO should not be null");
        employeeDTO.ifPresent(dto -> {
            assertEquals(1L, dto.getEmployeeDTOId(), "EmployeeDTOId should be 1");
            assertEquals("John Doe", dto.getEmployeeDTOName(), "EmployeeDTOName should be 'John Doe'");
            assertEquals("HR", dto.getDepartmentDTO().getDepartmentDTOName(), "EmployeeDTO's department should be 'HR'");
        });

        given(repository.findById(2L)).willReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> service.getEmployeeById(2L), "Expected EmployeeNotFoundException");
    }

    @Test
    void updateEmployee() {
        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(1L);
        existingEmployee.setEmployeeName("John Doe");
        existingEmployee.setDepartmentId(1L);

        EmployeeDTO updateEmployeeDTO = new EmployeeDTO(1L, "John Smith", 1L, null);

        given(repository.findById(1L)).willReturn(Optional.of(existingEmployee));
        given(repository.save(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        EmployeeDTO result = service.updateEmployee(1L, updateEmployeeDTO);

        assertNotNull(result, "The result should not be null");
        assertEquals(1L, result.getEmployeeDTOId(), "The EmployeeDTOId should not be 1");
        assertEquals("John Smith", result.getEmployeeDTOName(), "The EmployeeDTOName should be 'John Smith'");

        assertEquals("John Smith", existingEmployee.getEmployeeName(), "The employee's name should be updated to 'John Smith'");

        given(repository.findById(2L)).willReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> service.updateEmployee(2L, updateEmployeeDTO), "Expected EmployeeNotFoundException");
    }

    @Test
    void delete() {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("John Doe");

        given(repository.existsById(1L)).willReturn(true);

        assertDoesNotThrow(() -> service.delete(1L), "Expected delete method to not throw any exception");

        verify(repository).deleteById(1L);

        given(repository.existsById(2L)).willReturn(false);
        assertThrows(EmployeeNotFoundException.class, () -> service.delete(2L), "Expected EmployeeNotFoundException");

        verify(repository, never()).deleteById(2L);
    }
}
