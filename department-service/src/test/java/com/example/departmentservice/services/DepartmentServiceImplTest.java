package com.example.departmentservice.services;

import com.example.departmentservice.dtos.DepartmentDTO;
import com.example.departmentservice.entities.Department;
import com.example.departmentservice.exceptions.DepartmentNotFoundException;
import com.example.departmentservice.repositories.DepartmentRepository;
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
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentServiceImpl service;

    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(service, "modelMapper", modelMapper);
        // Настройка маппинга DTO -> Entity
        modelMapper.typeMap(DepartmentDTO.class, Department.class).addMappings(mapper -> {
            mapper.map(DepartmentDTO::getDepartmentDTOId, Department::setDepartmentId);
            mapper.map(DepartmentDTO::getDepartmentDTOName, Department::setDepartmentName);
        });

        // Настройка маппинга Entity -> DTO
        modelMapper.typeMap(Department.class, DepartmentDTO.class).addMappings(mapper -> {
            mapper.map(Department::getDepartmentId, DepartmentDTO::setDepartmentDTOId);
            mapper.map(Department::getDepartmentName, DepartmentDTO::setDepartmentDTOName);
        });

        ReflectionTestUtils.setField(service, "modelMapper", modelMapper);
    }

    @Test
    void createDepartment() {
        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentName("16");

        DepartmentDTO departmentDTO = new DepartmentDTO(1L, "16");

        given(repository.save(any(Department.class))).willReturn(department);

        DepartmentDTO createdDepartment = service.createDept(departmentDTO);

        assertNotNull(createdDepartment, "The createdDepartment should not be null");
        assertNotNull(createdDepartment.getDepartmentDTOId(), "The DepartmentDTOId should not be null");
        assertNotNull(createdDepartment.getDepartmentDTOName(), "The DepartmentDTOName should not be null");

        assertEquals(1L, createdDepartment.getDepartmentDTOId(), "DepartmentDTOId should be 1");
        assertEquals("16", createdDepartment.getDepartmentDTOName(), "DepartmentDTOName should be '16'");
    }

    @Test
    void getAllDepartments() {
        // Создаем список отделов для тестирования
        Department department1 = new Department();
        department1.setDepartmentId(1L);
        department1.setDepartmentName("16");

        Department department2 = new Department();
        department2.setDepartmentId(2L);
        department2.setDepartmentName("20");

        List<Department> departments = Arrays.asList(department1, department2);

        // Настраиваем репозиторий для возврата этого списка
        given(repository.findAll()).willReturn(departments);

        // Вызываем тестируемый метод
        List<DepartmentDTO> departmentDTOs = service.getAllDepts();

        // Проверяем, что результат не пустой и соответствует ожидаемому
        assertNotNull(departmentDTOs, "The departmentDTOs should not be null");
        assertEquals(2, departmentDTOs.size(), "The size of departmentDTOs should be 2");

        DepartmentDTO departmentDTO1 = departmentDTOs.get(0);
        DepartmentDTO departmentDTO2 = departmentDTOs.get(1);

        assertEquals(1L, departmentDTO1.getDepartmentDTOId(), "The first department's ID should be 1");
        assertEquals("16", departmentDTO1.getDepartmentDTOName(), "The first department's name should be '16'");

        assertEquals(2L, departmentDTO2.getDepartmentDTOId(), "The second department's ID should be 2");
        assertEquals("20", departmentDTO2.getDepartmentDTOName(), "The second department's name should be '20'");
    }

    @Test
    void getDepartmentById() {
        // Создаем отдел для тестирования
        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentName("16");

        // Настраиваем репозиторий для возврата этого отдела по ID
        given(repository.findById(1L)).willReturn(Optional.of(department));

        // Вызываем тестируемый метод
        Optional<DepartmentDTO> departmentDTO = service.getOneDeptById(1L);

        // Проверяем, что результат не пустой и соответствует ожидаемому
        assertNotNull(departmentDTO, "The departmentDTO should not be null");
        departmentDTO.ifPresent(dto -> {
            assertEquals(1L, dto.getDepartmentDTOId(), "DepartmentDTOId should be 1");
            assertEquals("16", dto.getDepartmentDTOName(), "DepartmentDTOId should be '16");
        });

        // Проверяем случай, когда отдел не найден
        given(repository.findById(2L)).willReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> service.getOneDeptById(2L), "Expected DepartmentNotFoundException");
    }

    @Test
    void updateDepartment() {
        // Создаем существующий отдел для тестирования
        Department existingDepartment = new Department();
        existingDepartment.setDepartmentId(1L);
        existingDepartment.setDepartmentName("16");

        // Создаем DTO с новыми данными отдела
        DepartmentDTO updateDepartmentDTO = new DepartmentDTO(1L, "20");

        // Настраиваем репозиторий для возврата существующего отдела по ID
        given(repository.findById(1L)).willReturn(Optional.of(existingDepartment));

        // Настраиваем репозиторий для сохранения обновленного отдела
        given(repository.save(any(Department.class))).willAnswer(invocation -> invocation.getArgument(0));

        // Вызываем тестируемый метод
        DepartmentDTO result = service.updateDept(1L, updateDepartmentDTO);

        // Проверяем, что результат не пустой и соответствует ожидаемому
        assertNotNull(result, "The result should not be null");
        assertEquals(1L, result.getDepartmentDTOId(), "The DepartmentDTOId should not be 1");
        assertEquals("20", result.getDepartmentDTOName(), "The DepartmentDTOName should be '20");

        // Проверяем, что данные отдела были обновлены
        assertEquals("20", existingDepartment.getDepartmentName(), "The department's name should be updated to '20");

        // Проверяем случай, когда отдел не найден
        given(repository.findById(2L)).willReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> service.updateDept(2L, updateDepartmentDTO), "Expected DepartmentNotFoundException");
    }

    @Test
    void delete() {
        // Создаем отдел для тестирования
        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentName("16");

        // Настраиваем репозиторий для возврата отдел по ID
        given(repository.existsById(1L)).willReturn(true);

        // Вызываем тестируемый метод
        assertDoesNotThrow(() -> service.deleteDept(1L), "Expected delete method to not throw any exception");

        // Проверяем, что метод репозитория для удаления был вызван
        verify(repository).deleteById(1L);

        // Проверяем случай, когда отдел не найден
        given(repository.existsById(2L)).willReturn(false);
        assertThrows(DepartmentNotFoundException.class, () -> service.deleteDept(2L), "Expected DepartmentNotFoundException");

        // Проверяем, что метод репозитория для удаления не был вызван для несуществующего отдела
        verify(repository, never()).deleteById(2L);
    }
}