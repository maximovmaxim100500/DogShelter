package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.PhotoReport;
import com.assistance.DogShelter.db.repository.PhotoReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для работы с фото отчетами.
 */
@Service
public class PhotoReportService {

    private final PhotoReportRepository photoReportRepository;

    @Autowired
    public PhotoReportService(PhotoReportRepository photoReportRepository) {
        this.photoReportRepository = photoReportRepository;
    }

    /**
     * Добавляет новый фото отчет.
     *
     * @param photoReport Фото отчет для добавления.
     * @return Добавленный фото отчет.
     */
    public PhotoReport addPhotoReport(PhotoReport photoReport) {
        return photoReportRepository.save(photoReport);
    }

    /**
     * Находит фото отчет по идентификатору.
     *
     * @param id Идентификатор фото отчета.
     * @return Фото отчет с указанным идентификатором, если найден.
     */
    public Optional<PhotoReport> findPhotoReportById(Long id) {
        return photoReportRepository.findById(id);
    }

    /**
     * Обновляет данные фото отчета.
     *
     * @param photoReport Фото отчет с обновленными данными.
     * @return Обновленный фото отчет.
     */
    public PhotoReport editPhotoReport(PhotoReport photoReport) {
        return photoReportRepository.save(photoReport);
    }

    /**
     * Удаляет фото отчет по идентификатору.
     *
     * @param id Идентификатор фото отчета для удаления.
     */
    public void deletePhotoReport(Long id) {
        photoReportRepository.deleteById(id);
    }
}