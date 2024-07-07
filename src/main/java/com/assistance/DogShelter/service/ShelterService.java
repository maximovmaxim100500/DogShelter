package com.assistance.DogShelter.service;

import com.assistance.DogShelter.model.Shelter;
import com.assistance.DogShelter.repositories.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с приютами.
 */
@Service
public class ShelterService {

    private final ShelterRepository shelterRepository;

    @Autowired
    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * Добавляет новый приют.
     *
     * @param shelter Приют для добавления.
     * @return Добавленный приют.
     */
    public Shelter addShelter(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    /**
     * Находит приют по идентификатору.
     *
     * @param id Идентификатор приюта.
     * @return Приют с указанным идентификатором, если найден.
     */
    public Shelter findShelterById(Long id) {
        Optional<Shelter> shelterOptional = shelterRepository.findById(id);
        return shelterOptional.orElse(null);
    }

    /**
     * Обновляет данные приюта.
     *
     * @param shelter Приют с обновленными данными.
     * @return Обновленный приют.
     */
    public Shelter editShelter(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    /**
     * Удаляет приют по идентификатору.
     *
     * @param id Идентификатор приюта для удаления.
     */
    public void deleteShelter(Long id) {
        shelterRepository.deleteById(id);
    }

    /**
     * Возвращает список всех приютов.
     *
     * @return Список всех приютов.
     */
    public List<Shelter> getAllShelters() {
        return shelterRepository.findAll();
    }
}
