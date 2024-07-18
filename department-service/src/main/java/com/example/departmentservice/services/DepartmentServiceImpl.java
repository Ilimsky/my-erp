package com.example.departmentservice.services;

import com.example.departmentservice.dtos.DepartmentDTO;
import com.example.departmentservice.entities.Department;
import com.example.departmentservice.exceptions.DepartmentNotFoundException;
import com.example.departmentservice.repositories.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    DepartmentRepository repository;
    private final ModelMapper modelMapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentDTO createDept(DepartmentDTO departmentDTO) {
        // Преобразуем DTO в сущность Department
        System.out.println("Mapping DepartmentDTO to Department");
        Department department = modelMapper.map(departmentDTO, Department.class);
        System.out.println("Mapped Department: " + department);

        // Сохраняем отдел в базу данных
        System.out.println("Saving Department to repository");
        department = repository.save(department);
        System.out.println("Saved Department: " + department);

        // Преобразуем сохраненную сущность обратно в DTO и возвращаем
        System.out.println("Mapping Department to DepartmentDTO");
        DepartmentDTO result = modelMapper.map(department, DepartmentDTO.class);
        System.out.println("Mapped DepartmentDTO: " + result);

        return result;
    }

    @Override
    public List<DepartmentDTO> getAllDepts() {
        // Получаем все отделы из базы данных
        List<Department> departments = repository.findAll();
        // Преобразуем список сущностей в список DTO и возвращаем
        return departments.stream()
                .map(e -> modelMapper.map(e, DepartmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DepartmentDTO> getOneDeptById(Long id) {
        // Ищем отдел по идентификатору в базе данных
        Optional<Department> departmentOptional = repository.findById(id);
        // Если сотрудник не найден, выбрасываем исключение
        if (departmentOptional.isEmpty()) {
            throw new DepartmentNotFoundException("Department with id " + id + " not found");
        }
        // Если сотрудник найден, преобразуем его в DTO и возвращаем
        return departmentOptional.map(e -> modelMapper.map(e, DepartmentDTO.class));
    }

    @Override
    public DepartmentDTO updateDept(Long id, DepartmentDTO departmentDTO) {
        // Ищем отдел по идентификатору в базе данных
        Optional<Department> departmentOptional = repository.findById(id);
        if (departmentOptional.isPresent()) {
            // Если отдел найден, обновляем его данные
            Department department = departmentOptional.get();
            department.setDepartmentName(departmentDTO.getDepartmentDTOName());
//            department.setDepartment(modelMapper.map(departmentDTO.getDepartmentDTO(), Department.class));
            // Сохраняем обновленный отдел в базу данных
            department = repository.save(department);
            // Преобразуем сохраненную сущность обратно в DTO и возвращаем
            return modelMapper.map(department, DepartmentDTO.class);
        } else {
            // Если отдел не найден, выбрасываем исключение
            throw new DepartmentNotFoundException("Department with id " + id + " not found");
        }
    }

    @Override
    public void deleteDept(Long id) {
        // Проверяем, существует ли отдел с данным идентификатором
        if (!repository.existsById(id)) {
            throw new DepartmentNotFoundException("Department with id: " + id + " not found");
        }
        // Удаляем отдел из базы данных по идентификатору
        repository.deleteById(id);
    }
}
