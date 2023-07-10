package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.model.entity.Customer;
import com.udacity.jdnd.course3.critter.model.entity.Employee;
import com.udacity.jdnd.course3.critter.model.entity.Pet;
import com.udacity.jdnd.course3.critter.model.entity.Schedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    @Query("select s from Schedule s where :pet member of s.pets")
    Iterable<Schedule> findSchedulesByPet(@Param("pet") Pet pet);
    @Query("select s from Schedule s where :employee member of s.employees")
    Iterable<Schedule> findSchedulesByEmployees(@Param("employee") Employee employee);
    @Query("select s from Schedule s join s.pets pet where pet.owner = :owner")
    Iterable<Schedule> findSchedulesByCustomer(@Param("owner") Customer owner);
}
