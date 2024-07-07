package com.assistance.DogShelter.repositories;

import com.assistance.DogShelter.model.PetAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с аватарами питомцев.
 */
@Repository
public interface PetAvatarRepository extends JpaRepository<PetAvatar, Long> {
}
