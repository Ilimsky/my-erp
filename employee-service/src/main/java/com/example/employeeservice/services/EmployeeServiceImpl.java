package com.example.employeeservice.services;

import com.example.departmentservice.dtos.DepartmentDTO;
import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.entities.Employee;
import com.example.employeeservice.exceptions.EmployeeNotFoundException;
import com.example.employeeservice.feign.DepartmentClient;
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
    private final EmployeeRepository repository;

    // Автоматически внедряем зависимость от ModelMapper
    // ModelMapper используется для преобразования между сущностями и их DTO-объектами
    private final ModelMapper modelMapper;

    private final DepartmentClient departmentClient;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository, ModelMapper modelMapper, DepartmentClient departmentClient) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.departmentClient = departmentClient;
    }

    /**
     * Создает нового сотрудника.
     *
     * @param employeeDTO объект EmployeeDTO с данными нового сотрудника.
     * @return EmployeeDTO объект с сохраненными данными сотрудника.
     */
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Преобразуем DTO в сущность Employee
        System.out.println("Mapping EmployeeDTO to Employee");
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        System.out.println("Mapped Employee: " + employee);

        // Сохраняем сотрудника в базу данных
        System.out.println("Saving Employee to repository");
        employee = repository.save(employee);
        System.out.println("Saved Employee: " + employee);

        // Преобразуем сохраненную сущность обратно в DTO и возвращаем
        System.out.println("Mapping Employee to EmployeeDTO");
        EmployeeDTO result = modelMapper.map(employee, EmployeeDTO.class);
        System.out.println("Mapped EmployeeDTO: " + result);

        return result;
    }


    /**
     * Возвращает список всех сотрудников.
     *
     * @return список объектов EmployeeDTO.
     */
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = repository.findAll();
        return employees.stream()
                .map(e -> {
                    EmployeeDTO employeeDTO = modelMapper.map(e, EmployeeDTO.class);
                    DepartmentDTO departmentDTO = departmentClient.getDepartmentById(e.getDepartmentId());
                    employeeDTO.setDepartmentDTO(departmentDTO);
                    return employeeDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * Возвращает сотрудника по его идентификатору.
     *
     * @param id идентификатор сотрудника.
     * @return Optional с объектом EmployeeDTO, если сотрудник найден.
     */

    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        Optional<Employee> employeeOptional = repository.findById(id);
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
        EmployeeDTO employeeDTO = modelMapper.map(employeeOptional.get(), EmployeeDTO.class);
        DepartmentDTO departmentDTO = departmentClient.getDepartmentById(employeeOptional.get().getDepartmentId());
        employeeDTO.setDepartmentDTO(departmentDTO);
        return Optional.of(employeeDTO);
    }

    /**
     * Обновляет данные сотрудника.
     *
     * @param id          идентификатор сотрудника.
     * @param employeeDTO объект EmployeeDTO с новыми данными сотрудника.
     * @return обновленный объект EmployeeDTO.
     * @throws EmployeeNotFoundException если сотрудник не найден.
     */
    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Optional<Employee> employeeOptional = repository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setEmployeeName(employeeDTO.getEmployeeDTOName());
            employee.setDepartmentId(employeeDTO.getDepartmentId());
            employee = repository.save(employee);
            return modelMapper.map(employee, EmployeeDTO.class);
        } else {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
    }

    /**
     * Удаляет сотрудника по его идентификатору.
     *
     * @param id идентификатор сотрудника.
     */
    @Override
    public void delete(Long id) {
        // Проверяем, существует ли сотрудник с данным идентификатором
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee with id: " + id + " not found");
        }
        // Удаляем сотрудника из базы данных по идентификатору
        repository.deleteById(id);
    }

}
