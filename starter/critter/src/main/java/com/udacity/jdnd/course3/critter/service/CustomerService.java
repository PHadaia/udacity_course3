package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.dto.CustomerDTO;
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
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    public CustomerService(CustomerRepository customerRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = customerRepository.save(createCustomerFromCustomerDTO(customerDTO));
        return createCustomerDTOFromCustomer(customer);
    }

    public List<CustomerDTO> getAllCustomers() {
        return StreamSupport
                .stream(customerRepository.findAll().spliterator(), false)
                .map(this::createCustomerDTOFromCustomer)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerByPet(long petId) {
        Optional<Customer> customer = customerRepository.findCustomerByPetId(petId);
        if(!customer.isPresent()) {
            throw new EntityNotFoundException();
        }

        return createCustomerDTOFromCustomer(customer.get());
    }

    private Customer createCustomerFromCustomerDTO(CustomerDTO customerDTO) {
        return new Customer(
                customerDTO.getId(),
                customerDTO.getName(),
                customerDTO.getPhoneNumber(),
                customerDTO.getNotes(),
                getPetsFromCustomerDTO(customerDTO)
        );
    }

    private List<Pet> getPetsFromCustomerDTO(CustomerDTO customerDTO) {
        List<Pet> petList = new ArrayList<>();
        if(null == customerDTO.getPetIds()) {
            return new ArrayList<>();
        }

        for(long id : customerDTO.getPetIds()) {
            Optional<Pet> pet = petRepository.findById(id);
            if(!pet.isPresent()) {
                throw new EntityNotFoundException();
            }

            petList.add(pet.get());
        }

        return petList;
    }

    private CustomerDTO createCustomerDTOFromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());
        customerDTO.setPetIds(customer.getPets().stream().map(Pet::getId).collect(Collectors.toList()));

        return  customerDTO;
    }
}
