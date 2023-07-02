package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.dto.PetDTO;
import com.udacity.jdnd.course3.critter.model.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    private PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public PetDTO savePet(PetDTO petDTO) {
        Pet pet = petRepository.save(createPetfromPetDTO(petDTO));
        return createDTOFromPet(pet);
    }

    public PetDTO getPet(Long petId) {
        Optional<Pet> pet = petRepository.findById(petId);

        if(!pet.isPresent()) {
            throw new EntityNotFoundException();
        }

        return createDTOFromPet(pet.get());
    }

    public List<PetDTO> getPets() {
        Iterable<Pet> petsIterable = petRepository.findAll();
        List<PetDTO> pets = new ArrayList<>();

        petsIterable.forEach(pet -> pets.add(createDTOFromPet(pet)));
        return pets;
    }

    public List<PetDTO> getPetsByOwner(Long ownerId) {
        Iterable<Pet> petsIterable = petRepository.findByOwnerId(ownerId);
        List<PetDTO> pets = new ArrayList<>();

        petsIterable.forEach(pet -> pets.add(createDTOFromPet(pet)));
        return pets;
    }

    private PetDTO createDTOFromPet(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setOwnerId(pet.getOwnerId());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());

        return petDTO;
    }

    private Pet createPetfromPetDTO(PetDTO petDTO) {
        return new Pet(
                null,
                petDTO.getType(),
                petDTO.getName(),
                petDTO.getOwnerId(),
                petDTO.getBirthDate(),
                petDTO.getNotes()
        );
    }
}
