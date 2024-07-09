package com.example.employeeservice.services;

import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.entities.Employee;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

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
        });

        // Настройка маппинга Entity -> DTO
        modelMapper.typeMap(Employee.class, EmployeeDTO.class).addMappings(mapper -> {
            mapper.map(Employee::getEmployeeId, EmployeeDTO::setEmployeeDTOId);
            mapper.map(Employee::getEmployeeName, EmployeeDTO::setEmployeeDTOName);
        });

        ReflectionTestUtils.setField(service, "modelMapper", modelMapper);
    }

    @Test
    void createEmployee() {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("John Doe");

        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "John Doe");

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
        // Создаем список сотрудников для тестирования
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1L);
        employee1.setEmployeeName("John Doe");

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2L);
        employee2.setEmployeeName("Jane Smith");

        List<Employee> employees = Arrays.asList(employee1, employee2);

        // Настраиваем репозиторий для возврата этого списка
        given(repository.findAll()).willReturn(employees);

        // Вызываем тестируемый метод
        List<EmployeeDTO> employeeDTOs = service.getAllEmployees();

        // Проверяем, что результат не пустой и соответствует ожидаемому
        assertNotNull(employeeDTOs, "The employeeDTOs should not be null");
        assertEquals(2, employeeDTOs.size(), "The size of employeeDTOs should be 2");

        EmployeeDTO employeeDTO1 = employeeDTOs.get(0);
        EmployeeDTO employeeDTO2 = employeeDTOs.get(1);

        assertEquals(1L, employeeDTO1.getEmployeeDTOId(), "The first employee's ID should be 1");
        assertEquals("John Doe", employeeDTO1.getEmployeeDTOName(), "The first employee's name should be 'John Doe'");

        assertEquals(2L, employeeDTO2.getEmployeeDTOId(), "The second employee's ID should be 2");
        assertEquals("Jane Smith", employeeDTO2.getEmployeeDTOName(), "The second employee's name should be 'Jane Smith'");
    }

    @Test
    void getEmployeeById() {
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void delete() {
    }
}