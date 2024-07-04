package com.assistance.DogShelter.service;
import com.assistance.DogShelter.model.Pet;
import com.assistance.DogShelter.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Сервис для работы с питомцами.
 */
@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Добавляет нового питомца.
     *
     * @param pet Питомец для добавления.
     * @return Добавленный питомец.
     */
    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }

    /**
     * Находит питомца по идентификатору.
     *
     * @param id Идентификатор питомца.
     * @return Питомец с указанным идентификатором, если найден.
     */
    public Optional<Pet> findPetById(Long id) {
        return petRepository.findById(id);
    }

    /**
     * Обновляет данные питомца.
     *
     * @param pet Питомец с обновленными данными.
     * @return Обновленный питомец.
     */
    public Pet editPet(Pet pet) {
        return petRepository.save(pet);
    }

    /**
     * Удаляет питомца по идентификатору.
     *
     * @param id Идентификатор питомца для удаления.
     */
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    /**
     * Возвращает список всех питомцев.
     *
     * @return Список всех питомцев.
     */
    public Collection<Pet> getAllPets() {
        return petRepository.findAll();
    }
}
