package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.model.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.model.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.save(createEmployeeFromEmployeeDTO(employeeDTO));
        return createEmployeeDTOFromEmployee(employee);
    }

    public EmployeeDTO getEmployee(Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if(!employee.isPresent()) {
            throw new EntityNotFoundException();
        }

        return createEmployeeDTOFromEmployee(employee.get());
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee == null) {
            throw new EntityNotFoundException();
        }

        employee.setDaysAvailable(daysAvailable);
        employee.setSkills(employee.getSkills());
        employeeRepository.save(employee);
    }

    public List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeRequestDTO) {
        return employeeRepository.findEmployeesByDaysAvailableAndSkills(
                        Set.of(employeeRequestDTO.getDate().getDayOfWeek()),
                        employeeRequestDTO.getSkills()
                )
                .stream()
                .map(this::createEmployeeDTOFromEmployee)
                .collect(Collectors.toList());
    }

    private EmployeeDTO createEmployeeDTOFromEmployee(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());
        return employeeDTO;
    }

    private Employee createEmployeeFromEmployeeDTO(EmployeeDTO employeeDTO) {
        return new Employee(
                null,
                employeeDTO.getName(),
                employeeDTO.getSkills(),
                employeeDTO.getDaysAvailable()
        );
    }
}
