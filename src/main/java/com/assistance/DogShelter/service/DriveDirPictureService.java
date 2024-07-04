package com.assistance.DogShelter.service;

import com.assistance.DogShelter.model.DriveDirPicture;
import com.assistance.DogShelter.repositories.DriveDirPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для работы с изображениями, загруженными в хранилище.
 */
@Service
public class DriveDirPictureService {

    private final DriveDirPictureRepository driveDirPictureRepository;

    @Autowired
    public DriveDirPictureService(DriveDirPictureRepository driveDirPictureRepository) {
        this.driveDirPictureRepository = driveDirPictureRepository;
    }

    /**
     * Добавляет новое изображение в хранилище.
     *
     * @param driveDirPicture Изображение для добавления.
     * @return Добавленное изображение.
     */
    public DriveDirPicture addDriveDirPicture(DriveDirPicture driveDirPicture) {
        return driveDirPictureRepository.save(driveDirPicture);
    }

    /**
     * Находит изображение по идентификатору.
     *
     * @param id Идентификатор изображения.
     * @return Изображение с указанным идентификатором, если найдено.
     */
    public Optional<DriveDirPicture> findDriveDirPictureById(Long id) {
        return driveDirPictureRepository.findById(id);
    }

    /**
     * Обновляет данные изображения.
     *
     * @param driveDirPicture Изображение с обновленными данными.
     * @return Обновленное изображение.
     */
    public DriveDirPicture editDriveDirPicture(DriveDirPicture driveDirPicture) {
        return driveDirPictureRepository.save(driveDirPicture);
    }

    /**
     * Удаляет изображение по идентификатору.
     *
     * @param id Идентификатор изображения для удаления.
     */
    public void deleteDriveDirPicture(Long id) {
        driveDirPictureRepository.deleteById(id);
    }
}
