package com.assistance.DogShelter.service;

import com.assistance.DogShelter.model.DriveDirPicture;
import com.assistance.DogShelter.model.Shelter;
import com.assistance.DogShelter.repositories.DriveDirPictureRepository;
import com.assistance.DogShelter.repositories.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Сервис для работы с изображениями, загруженными в хранилище.
 */
@Service
public class DriveDirPictureService {

    private final DriveDirPictureRepository driveDirPictureRepository;
    private final ShelterRepository shelterRepository;

    @Autowired
    public DriveDirPictureService(DriveDirPictureRepository driveDirPictureRepository, ShelterRepository shelterRepository) {
        this.driveDirPictureRepository = driveDirPictureRepository;
        this.shelterRepository = shelterRepository;
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
     * Загружает изображение в хранилище.
     *
     * @param file       Файл изображения для загрузки.
     * @param shelterId  Идентификатор приюта.
     * @return Загруженное изображение.
     * @throws IOException Если произошла ошибка при загрузке файла.
     */
    public DriveDirPicture uploadPicture(MultipartFile file, Long shelterId) throws IOException {
        Optional<Shelter> shelterOpt = shelterRepository.findById(shelterId);
        if (!shelterOpt.isPresent()) {
            throw new IllegalArgumentException("Shelter not found");
        }

        String filePath = Paths.get("uploads", file.getOriginalFilename()).toString();
        Files.createDirectories(Paths.get("uploads"));
        Files.write(Paths.get(filePath), file.getBytes());

        DriveDirPicture driveDirPicture = new DriveDirPicture();
        driveDirPicture.setFilePath(filePath);
        driveDirPicture.setFileSize(file.getSize());
        driveDirPicture.setMediaType(file.getContentType());
        driveDirPicture.setData(file.getBytes());
        driveDirPicture.setShelter(shelterOpt.get());

        return addDriveDirPicture(driveDirPicture);
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