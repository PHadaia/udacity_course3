package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.model.entity.Employee;
import com.udacity.jdnd.course3.critter.model.entity.Pet;
import com.udacity.jdnd.course3.critter.model.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScheduleService {
    ScheduleRepository scheduleRepository;
    EmployeeRepository employeeRepository;
    PetRepository petRepository;
    CustomerRepository customerRepository;

    public ScheduleService(
            ScheduleRepository scheduleRepository,
            EmployeeRepository employeeRepository,
            PetRepository petRepository,
            CustomerRepository customerRepository
    ) {
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public ScheduleDTO saveSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = createScheduleFromScheduleDTO(scheduleDTO);
        schedule = scheduleRepository.save(schedule);
        return createScheduleDTOFromSchedule(schedule);
    }

    public List<ScheduleDTO> getAllSchedules() {
        return StreamSupport
                .stream(scheduleRepository.findAll().spliterator(), false)
                .map(this::createScheduleDTOFromSchedule)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForPet(long petId) {
        return StreamSupport
                .stream(scheduleRepository.findSchedulesByPet(petRepository.findById(petId).orElseThrow(EntityNotFoundException::new)).spliterator(), false)
                .map(this::createScheduleDTOFromSchedule)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        return StreamSupport
                .stream(scheduleRepository.findSchedulesByEmployees(employeeRepository.findById(employeeId).orElseThrow(EntityNotFoundException::new)).spliterator(), false)
                .map(this::createScheduleDTOFromSchedule)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
        return StreamSupport
                .stream(scheduleRepository.findSchedulesByCustomer(customerRepository.findById(customerId).orElseThrow(EntityNotFoundException::new)).spliterator(), false)
                .map(this::createScheduleDTOFromSchedule)
                .collect(Collectors.toList());
    }

    private Schedule createScheduleFromScheduleDTO(ScheduleDTO scheduleDTO) {
        return new Schedule(
                scheduleDTO.getId(),
                getEmployeeListFromIdList(scheduleDTO.getEmployeeIds()),
                getPetListFromIdLIst(scheduleDTO.getPetIds()),
                scheduleDTO.getDate(),
                scheduleDTO.getActivities()
        );
    }

    private ScheduleDTO createScheduleDTOFromSchedule(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
        scheduleDTO.setPetIds(schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());
        return scheduleDTO;
    }

    private List<Employee> getEmployeeListFromIdList(List<Long> employeeIds) {
        return employeeIds.stream().map(id -> employeeRepository.findById(id)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    private List<Pet> getPetListFromIdLIst(List<Long> petIds) {
        return petIds.stream().map(id -> petRepository.findById(id)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }
}
