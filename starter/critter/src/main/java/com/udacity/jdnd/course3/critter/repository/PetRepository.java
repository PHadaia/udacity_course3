package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.model.entity.Pet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {
    @Query("select p from Pet p where p.owner.id = :ownerId")
    Iterable<Pet> findByOwnerId(@Param("ownerId") Long ownerId);
}
