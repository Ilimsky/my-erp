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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DepartmentClient departmentClient;

    @InjectMocks
    private EmployeeServiceImpl service;

    private Employee employee;
    private EmployeeDTO employeeDTO;
    private DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .employeeId(1L)
                .employeeName("John Doe")
                .departmentId(1L)
                .build();

        employeeDTO = EmployeeDTO.builder()
                .employeeDTOId(1L)
                .employeeDTOName("John Doe")
                .departmentId(1L)
                .build();

        departmentDTO = DepartmentDTO.builder()
                .departmentDTOId(1L)
                .departmentDTOName("HR")
                .build();
    }

    @Test
    void createEmployee_Success() {
        when(modelMapper.map(any(EmployeeDTO.class), any(Class.class))).thenReturn(employee);
        when(repository.save(any(Employee.class))).thenReturn(employee);
        when(modelMapper.map(any(Employee.class), any(Class.class))).thenReturn(employeeDTO);
        when(departmentClient.getDepartmentById(any(Long.class))).thenReturn(departmentDTO);

        EmployeeDTO result = service.createEmployee(employeeDTO);

        assertNotNull(result);
        assertEquals(employeeDTO.getEmployeeDTOId(), result.getEmployeeDTOId());
        verify(repository, times(2)).save(any(Employee.class));
    }

    @Test
    void getAllEmployees_Success() {
        List<Employee> employees = Collections.singletonList(employee);
        when(repository.findAll()).thenReturn(employees);
        when(modelMapper.map(any(Employee.class), any(Class.class))).thenReturn(employeeDTO);

        List<EmployeeDTO> result = service.getAllEmployees();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getEmployeeById_NotFound() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> service.getEmployeeById(1L));
    }

    @Test
    void updateEmployee_Success() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(employee));
        when(repository.save(any(Employee.class))).thenReturn(employee);
        when(modelMapper.map(any(Employee.class), any(Class.class))).thenReturn(employeeDTO);

        EmployeeDTO result = service.updateEmployee(1L, employeeDTO);

        assertNotNull(result);
        assertEquals(employeeDTO.getEmployeeDTOId(), result.getEmployeeDTOId());
        verify(repository, times(1)).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_Success() {
        when(repository.existsById(any(Long.class))).thenReturn(true);

        service.delete(1L);

        verify(repository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void deleteEmployee_NotFound() {
        when(repository.existsById(any(Long.class))).thenReturn(false);

        assertThrows(EmployeeNotFoundException.class, () -> service.delete(1L));
    }
}
