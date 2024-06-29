package com.example.employeeservice.services;

import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.entities.Department;
import com.example.employeeservice.entities.Employee;
import com.example.employeeservice.exceptions.EmployeeNotFoundException;
import com.example.employeeservice.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Аннотация Spring, указывающая, что этот класс является сервисом (компонентом службы).
//Обозначает, что класс должен быть зарегистрирован как Spring Bean, и его экземпляр будет управляться Spring контейнером.
@Service
public class EmployeeServiceImpl implements EmployeeService {

    // Автоматически внедряем зависимость от репозитория EmployeeRepository.
    // Это позволяет использовать методы репозитория для доступа к данным сотрудников в базе данных
    @Autowired
    private EmployeeRepository repository;

    // Автоматически внедряем зависимость от ModelMapper
    // ModelMapper используется для преобразования между сущностями и их DTO-объектами
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Создает нового сотрудника.
     * @param employeeDTO объект EmployeeDTO с данными нового сотрудника.
     * @return EmployeeDTO объект с сохраненными данными сотрудника.
     */
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Преобразуем DTO в сущность Employee
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        // Сохраняем сотрудника в базу данных
        employee = repository.save(employee);
        // Преобразуем сохраненную сущность обратно в DTO и возвращаем
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    /**
     * Возвращает список всех сотрудников.
     * @return список объектов EmployeeDTO.
     */
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        // Получаем всех сотрудников из базы данных
        List<Employee> employees = repository.findAll();
        // Преобразуем список сущностей в список DTO и возвращаем
        return employees.stream()
                .map(e -> modelMapper.map(e, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает сотрудника по его идентификатору.
     * @param id идентификатор сотрудника.
     * @return Optional с объектом EmployeeDTO, если сотрудник найден.
     */
    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        // Ищем сотрудника по идентификатору в базе данных
        Optional<Employee> employeeOptional = repository.findById(id);
        // Если сотрудник не найден, выбрасываем исключение
        if (employeeOptional.isEmpty()){
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
        // Если сотрудник найден, преобразуем его в DTO и возвращаем
        return employeeOptional.map(e -> modelMapper.map(e, EmployeeDTO.class));
    }

    /**
     * Обновляет данные сотрудника.
     * @param id идентификатор сотрудника.
     * @param employeeDTO объект EmployeeDTO с новыми данными сотрудника.
     * @return обновленный объект EmployeeDTO.
     * @throws EmployeeNotFoundException если сотрудник не найден.
     */
    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        // Ищем сотрудника по идентификатору в базе данных
        Optional<Employee> employeeOptional = repository.findById(id);
        if (employeeOptional.isPresent()){
            // Если сотрудник найден, обновляем его данные
            Employee employee = employeeOptional.get();
            employee.setEmployeeName(employeeDTO.getEmployeeDTOName());
            employee.setDepartment(modelMapper.map(employeeDTO.getDepartmentDTO(), Department.class));
            // Сохраняем обновленного сотрудника в базу данных
            employee = repository.save(employee);
            // Преобразуем сохраненную сущность обратно в DTO и возвращаем
            return modelMapper.map(employee, EmployeeDTO.class);
        } else {
            // Если сотрудник не найден, выбрасываем исключение
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
    }

    /**
     * Удаляет сотрудника по его идентификатору.
     * @param id идентификатор сотрудника.
     */
    @Override
    public void delete(Long id) {
        // Удаляем сотрудника из базы данных по идентификатору
        repository.deleteById(id);
    }

    /**
     * Возвращает список сотрудников по идентификатору отдела.
     * @param departmentId идентификатор отдела.
     * @return список объектов EmployeeDTO.
     */
    @Override
    public List<EmployeeDTO> getEmployeesByDepartmentId(Long departmentId) {
        // Получаем всех сотрудников из базы данных и фильтруем по идентификатору отдела
        List<Employee> employees = repository.findAll().stream()
                .filter(e -> e.getDepartment().getDepartmentId().equals(departmentId))
                .toList();
        // Преобразуем список сущностей в список DTO и возвращаем
        return employees.stream()
                .map(e -> modelMapper.map(e, EmployeeDTO.class))
                .collect(Collectors.toList());
    }
}
