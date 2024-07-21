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
import java.util.stream.Collectors;

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
        DepartmentDTO departmentDTO = new DepartmentDTO(1L, "IT Department");

        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("John Doe");
        employee.setDepartmentId(departmentDTO.getDepartmentDTOId());
        employee.setDepartmentName(departmentDTO.getDepartmentDTOName());

        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "John Doe", departmentDTO.getDepartmentDTOId(), departmentDTO.getDepartmentDTOName(), departmentDTO);

        given(departmentClient.getDepartmentById(1L)).willReturn(departmentDTO);
        given(repository.save(any(Employee.class))).willReturn(employee);

        EmployeeDTO createdEmployee = service.createEmployee(employeeDTO);

        assertNotNull(createdEmployee, "The createdEmployee should not be null");
        assertNotNull(createdEmployee.getEmployeeDTOId(), "The EmployeeDTOId should not be null");
        assertNotNull(createdEmployee.getEmployeeDTOName(), "The EmployeeDTOName should not be null");

        assertEquals(1L, createdEmployee.getEmployeeDTOId(), "EmployeeDTOId should be 1");
        assertEquals("John Doe", createdEmployee.getEmployeeDTOName(), "EmployeeDTOName should be 'John Doe'");
        assertEquals(departmentDTO, createdEmployee.getDepartmentDTO(), "The departmentDTO should match the expected department");
    }

    @Test
    void createEmployees() {
        DepartmentDTO departmentDTO1 = new DepartmentDTO(1L, "IT Department");
        DepartmentDTO departmentDTO2 = new DepartmentDTO(2L, "HR Department");

        Employee employee1 = new Employee();
        employee1.setEmployeeId(1L);
        employee1.setEmployeeName("John Doe");
        employee1.setDepartmentId(departmentDTO1.getDepartmentDTOId());
        employee1.setDepartmentName(departmentDTO1.getDepartmentDTOName());

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2L);
        employee2.setEmployeeName("Jane Smith");
        employee2.setDepartmentId(departmentDTO2.getDepartmentDTOId());
        employee2.setDepartmentName(departmentDTO2.getDepartmentDTOName());

        EmployeeDTO employeeDTO1 = new EmployeeDTO(1L, "John Doe", departmentDTO1.getDepartmentDTOId(), departmentDTO1.getDepartmentDTOName(), departmentDTO1);
        EmployeeDTO employeeDTO2 = new EmployeeDTO(2L, "Jane Smith", departmentDTO2.getDepartmentDTOId(), departmentDTO2.getDepartmentDTOName(), departmentDTO2);

        given(departmentClient.getDepartmentById(1L)).willReturn(departmentDTO1);
        given(departmentClient.getDepartmentById(2L)).willReturn(departmentDTO2);
        given(repository.save(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        List<EmployeeDTO> employeeDTOs = Arrays.asList(employeeDTO1, employeeDTO2);
        List<EmployeeDTO> createdEmployees = employeeDTOs.stream().map(service::createEmployee).collect(Collectors.toList());

        assertNotNull(createdEmployees, "The createdEmployees list should not be null");
        assertEquals(2, createdEmployees.size(), "The size of createdEmployees list should be 2");

        EmployeeDTO createdEmployee1 = createdEmployees.get(0);
        EmployeeDTO createdEmployee2 = createdEmployees.get(1);

        assertNotNull(createdEmployee1, "The createdEmployee1 should not be null");
        assertEquals(1L, createdEmployee1.getEmployeeDTOId(), "EmployeeDTOId should be 1");
        assertEquals("John Doe", createdEmployee1.getEmployeeDTOName(), "EmployeeDTOName should be 'John Doe'");
        assertEquals(departmentDTO1, createdEmployee1.getDepartmentDTO(), "The departmentDTO1 should match the expected department");

        assertNotNull(createdEmployee2, "The createdEmployee2 should not be null");
        assertEquals(2L, createdEmployee2.getEmployeeDTOId(), "EmployeeDTOId should be 2");
        assertEquals("Jane Smith", createdEmployee2.getEmployeeDTOName(), "EmployeeDTOName should be 'Jane Smith'");
        assertEquals(departmentDTO2, createdEmployee2.getDepartmentDTO(), "The departmentDTO2 should match the expected department");
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
        // Создаем сотрудника для тестирования
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("John Doe");

        // Настраиваем репозиторий для возврата этого сотрудника по ID
        given(repository.findById(1L)).willReturn(Optional.of(employee));

        // Вызываем тестируемый метод
        Optional<EmployeeDTO> employeeDTO = service.getEmployeeById(1L);

        // Проверяем, что результат не пустой и соответствует ожидаемому
        assertNotNull(employeeDTO, "The employeeDTO should not be null");
        employeeDTO.ifPresent(dto -> {
            assertEquals(1L, dto.getEmployeeDTOId(), "EmployeeDTOId should be 1");
            assertEquals("John Doe", dto.getEmployeeDTOName(), "EmployeeDTOId should be 'John Doe");
        });

        // Проверяем случай, когда сотрудник не найден
        given(repository.findById(2L)).willReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> service.getEmployeeById(2L), "Expected EmployeeNotFoundException");
    }

    @Test
    void updateEmployee() {
        // Создаем существующего сотрудника для тестирования
        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(1L);
        existingEmployee.setEmployeeName("John Doe");

        // Создаем DTO с новыми данными сотрудника
        EmployeeDTO updateEmployeeDTO = new EmployeeDTO(1L, "John Smith");

        // Настраиваем репозиторий для возврата существующего сотрудника по ID
        given(repository.findById(1L)).willReturn(Optional.of(existingEmployee));

        // Настраиваем репозиторий для сохранения обновленного сотрудника
        given(repository.save(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        // Вызываем тестируемый метод
        EmployeeDTO result = service.updateEmployee(1L, updateEmployeeDTO);

        // Проверяем, что результат не пустой и соответствует ожидаемому
        assertNotNull(result, "The result should not be null");
        assertEquals(1L, result.getEmployeeDTOId(), "The EmployeeDTOId should not be 1");
        assertEquals("John Smith", result.getEmployeeDTOName(), "The EmployeeDTOName should be 'John Smith");

        // Проверяем, что данные сотрудника были обновлены
        assertEquals("John Smith", existingEmployee.getEmployeeName(), "The employee's name should be updated to 'John Smith");

        // Проверяем случай, когда сотрудник не найден
        given(repository.findById(2L)).willReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> service.updateEmployee(2L, updateEmployeeDTO), "Expected EmployeeNotFoundException");
    }

    @Test
    void delete() {
        // Создаем сотрудника для тестирования
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("John Doe");

        // Настраиваем репозиторий для возврата сотрудника по ID
        given(repository.existsById(1L)).willReturn(true);

        // Вызываем тестируемый метод
        assertDoesNotThrow(() -> service.delete(1L), "Expected delete method to not throw any exception");

        // Проверяем, что метод репозитория для удаления был вызван
        verify(repository).deleteById(1L);

        // Проверяем случай, когда сотрудник не найден
        given(repository.existsById(2L)).willReturn(false);
        assertThrows(EmployeeNotFoundException.class, () -> service.delete(2L), "Expected EmployeeNotFoundException");

        // Проверяем, что метод репозитория для удаления не был вызван для несуществующего сотрудника
        verify(repository, never()).deleteById(2L);
    }

}