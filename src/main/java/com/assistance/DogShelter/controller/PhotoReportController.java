package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.model.PhotoReport;
import com.assistance.DogShelter.service.PhotoReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с фото отчетами.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/photoReports")
public class PhotoReportController {

    private final PhotoReportService photoReportService;

    public PhotoReportController(PhotoReportService photoReportService) {
        this.photoReportService = photoReportService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового фото отчета.
     *
     * @param photoReport Переданный фото отчет в теле запроса.
     * @return Фото отчет, который был добавлен с помощью сервиса.
     */
    @PostMapping("/add")
    @Operation(summary = "Add photo report",
            description = "Adds a new photo report to the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<PhotoReport> addPhotoReport(@RequestBody PhotoReport photoReport) {
        PhotoReport addedPhotoReport = photoReportService.addPhotoReport(photoReport);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPhotoReport);
    }

    /**
     * Обрабатывает GET-запрос для поиска фото отчета по идентификатору.
     *
     * @param id Идентификатор фото отчета.
     * @return Фото отчет с указанным идентификатором, если найден, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    @Operation(summary = "Search photo report",
            description = "Searches for a photo report in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Photo report not found")
            })
    public ResponseEntity<PhotoReport> findPhotoReportById(@PathVariable Long id) {
        Optional<PhotoReport> photoReport = photoReportService.findPhotoReportById(id);
        return photoReport.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Обрабатывает PUT-запрос для редактирования фото отчета.
     *
     * @param photoReport Фото отчет с обновленными данными в теле запроса.
     * @param id         Идентификатор редактируемого фото отчета.
     * @return Обновленный фото отчет, если редактирование выполнено успешно, в противном случае возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    @Operation(summary = "Update photo report",
            description = "Updates a photo report's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Photo report not found")
            })
    public ResponseEntity<PhotoReport> editPhotoReport(@RequestBody PhotoReport photoReport, @PathVariable Long id) {
        Optional<PhotoReport> foundPhotoReport = photoReportService.findPhotoReportById(id);
        if (foundPhotoReport.isPresent()) {
            PhotoReport updatedPhotoReport = photoReportService.editPhotoReport(photoReport);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPhotoReport);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления фото отчета по его идентификатору.
     *
     * @param id Идентификатор фото отчета для удаления.
     * @return Подтверждение успешного удаления фото отчета.
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Remove photo report",
            description = "Removes a photo report from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Photo report not found")
            })
    public ResponseEntity<Void> deletePhotoReport(@PathVariable Long id) {
        photoReportService.deletePhotoReport(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}