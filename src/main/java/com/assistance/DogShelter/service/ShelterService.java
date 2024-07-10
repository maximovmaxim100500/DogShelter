package com.assistance.DogShelter.service;

import com.assistance.DogShelter.controller.dto.PetDto;
import com.assistance.DogShelter.controller.dto.ShelterDto;
import com.assistance.DogShelter.db.model.Pet;
import com.assistance.DogShelter.db.model.Shelter;
import com.assistance.DogShelter.db.repository.ShelterRepository;
import com.assistance.DogShelter.mapper.ShelterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для работы с приютами.
 */
@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final ShelterMapper shelterMapper;

    /**
     * Добавляет новый приют.
     *
     * @param shelterDto Приют для добавления.
     * @return Добавленный приют.
     */
    public ShelterDto addShelter(ShelterDto shelterDto) {
        Shelter shelter = shelterMapper.mapToShelter(shelterDto);
        Shelter savedShelter = shelterRepository.save(shelter);
        return shelterMapper.mapToShelterDto(savedShelter);
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
    public List<ShelterDto> getAllShelters() {
        List<Shelter> all = shelterRepository.findAll();
        return shelterMapper.mapToShelterDtos(all);
    }

}
