package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.PetAvatar;
import com.assistance.DogShelter.db.repository.PetAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для работы с аватарами питомцев.
 */
@Service
public class PetAvatarService {

    private final PetAvatarRepository petAvatarRepository;

    @Autowired
    public PetAvatarService(PetAvatarRepository petAvatarRepository) {
        this.petAvatarRepository = petAvatarRepository;
    }

    /**
     * Добавляет новый аватар питомца.
     *
     * @param petAvatar Аватар для добавления.
     * @return Добавленный аватар.
     */
    public PetAvatar addPetAvatar(PetAvatar petAvatar) {
        return petAvatarRepository.save(petAvatar);
    }

    /**
     * Находит аватар по идентификатору.
     *
     * @param id Идентификатор аватара.
     * @return Аватар с указанным идентификатором, если найден.
     */
    public Optional<PetAvatar> findPetAvatarById(Long id) {
        return petAvatarRepository.findById(id);
    }

    /**
     * Обновляет данные аватара.
     *
     * @param petAvatar Аватар с обновленными данными.
     * @return Обновленный аватар.
     */
    public PetAvatar editPetAvatar(PetAvatar petAvatar) {
        return petAvatarRepository.save(petAvatar);
    }

    /**
     * Удаляет аватар по идентификатору.
     *
     * @param id Идентификатор аватара для удаления.
     */
    public void deletePetAvatar(Long id) {
        petAvatarRepository.deleteById(id);
    }
}
