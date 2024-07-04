package com.assistance.DogShelter.repositories;

import com.assistance.DogShelter.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с отчетами.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}