package com.assistance.DogShelter.repositories;

import com.assistance.DogShelter.model.DriveDirPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с изображениями, загруженными в хранилище.
 */
@Repository
public interface DriveDirPictureRepository extends JpaRepository<DriveDirPicture, Long> {
    Optional<DriveDirPicture> findByShelterId(Long shelterId);
}