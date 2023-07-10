package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.dto.PetDTO;
import com.udacity.jdnd.course3.critter.model.entity.Customer;
import com.udacity.jdnd.course3.critter.model.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public PetDTO savePet(PetDTO petDTO) {
        Optional<Customer> customer = customerRepository.findById(petDTO.getOwnerId());
        if(!customer.isPresent()) {
            throw new EntityNotFoundException();
        }

        Pet pet = createPetfromPetDTO(petDTO);
        pet.setOwner(customer.get());
        pet = petRepository.save(pet);

        customer.get().setPets(List.of(pet));

        return createPetDTOFromPet(pet);
    }

    public PetDTO getPet(Long petId) {
        Optional<Pet> pet = petRepository.findById(petId);

        if(!pet.isPresent()) {
            throw new EntityNotFoundException();
        }

        return createPetDTOFromPet(pet.get());
    }

    public List<PetDTO> getPets() {
        return StreamSupport
                .stream(petRepository.findAll().spliterator(), false)
                .map(this::createPetDTOFromPet)
                .collect(Collectors.toList());
    }

    public List<PetDTO> getPetsByOwner(Long ownerId) {
        Iterable<Pet> petsIterable = petRepository.findByOwnerId(ownerId);
        List<PetDTO> pets = new ArrayList<>();

        petsIterable.forEach(pet -> pets.add(createPetDTOFromPet(pet)));
        return pets;
    }

    private PetDTO createPetDTOFromPet(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setOwnerId(pet.getOwner().getId());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());

        return petDTO;
    }

    private Pet createPetfromPetDTO(PetDTO petDTO) {
        return new Pet(
                null,
                petDTO.getType(),
                petDTO.getName(),
                getOwner(petDTO.getOwnerId()),
                petDTO.getBirthDate(),
                petDTO.getNotes()
        );
    }

    private Customer getOwner(long ownerId) {
        Optional<Customer> owner = customerRepository.findById(ownerId);
        return owner.orElse(null);
    }
}
