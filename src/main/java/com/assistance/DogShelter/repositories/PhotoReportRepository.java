package com.assistance.DogShelter.repositories;

import com.assistance.DogShelter.model.PhotoReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с фото отчетами.
 */
@Repository
public interface PhotoReportRepository extends JpaRepository<PhotoReport, Long> {
}