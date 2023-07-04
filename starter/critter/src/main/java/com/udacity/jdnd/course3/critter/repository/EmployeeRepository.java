package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.model.entity.Employee;
import com.udacity.jdnd.course3.critter.model.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Set;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
//    Set<Employee> findEmployeesByDaysAvailableAndSkillsIn(Set<DayOfWeek> dayOfWeek, Set<EmployeeSkill> employeeSkills);

    @Query("SELECT e from Employee e where e.daysAvailable in :daysOfWeek and e.skills in :employeeSkills")
    Set<Employee> findEmployeesByDaysAvailableAndSkills(
            @Param("daysOfWeek") Set<DayOfWeek> daysOfWeek,
            @Param("employeeSkills") Set<EmployeeSkill> employeeSkills
    );
}
