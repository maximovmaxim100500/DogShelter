package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.model.DriveDirPicture;
import com.assistance.DogShelter.service.DriveDirPictureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с изображениями, загруженными в хранилище.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/driveDirPictures")
public class DriveDirPictureController {

    private final DriveDirPictureService driveDirPictureService;

    public DriveDirPictureController(DriveDirPictureService driveDirPictureService) {
        this.driveDirPictureService = driveDirPictureService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового изображения в хранилище.
     *
     * @param driveDirPicture Переданное изображение в теле запроса.
     * @return Изображение, которое было добавлено с помощью сервиса.
     */
    @PostMapping("/add")
    @Operation(summary = "Add drive directory picture",
            description = "Adds a new drive directory picture to the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<DriveDirPicture> addDriveDirPicture(@RequestBody DriveDirPicture driveDirPicture) {
        DriveDirPicture addedDriveDirPicture = driveDirPictureService.addDriveDirPicture(driveDirPicture);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedDriveDirPicture);
    }

    /**
     * Обрабатывает GET-запрос для поиска изображения по идентификатору.
     *
     * @param id Идентификатор изображения.
     * @return Изображение с указанным идентификатором, если найдено, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    @Operation(summary = "Search drive directory picture",
            description = "Searches for a drive directory picture in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Drive directory picture not found")
            })
    public ResponseEntity<DriveDirPicture> findDriveDirPictureById(@PathVariable Long id) {
        Optional<DriveDirPicture> driveDirPicture = driveDirPictureService.findDriveDirPictureById(id);
        return driveDirPicture.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Обрабатывает PUT-запрос для редактирования изображения в хранилище.
     *
     * @param driveDirPicture Изображение с обновленными данными в теле запроса.
     * @param id              Идентификатор редактируемого изображения.
     * @return Обновленное изображение, если редактирование выполнено успешно, в противном случае возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    @Operation(summary = "Update drive directory picture",
            description = "Updates a drive directory picture's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Drive directory picture not found")
            })
    public ResponseEntity<DriveDirPicture> editDriveDirPicture(@RequestBody DriveDirPicture driveDirPicture, @PathVariable Long id) {
        Optional<DriveDirPicture> foundDriveDirPicture = driveDirPictureService.findDriveDirPictureById(id);
        if (foundDriveDirPicture.isPresent()) {
            DriveDirPicture updatedDriveDirPicture = driveDirPictureService.editDriveDirPicture(driveDirPicture);
            return ResponseEntity.status(HttpStatus.OK).body(updatedDriveDirPicture);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления изображения по его идентификатору.
     *
     * @param id Идентификатор изображения для удаления.
     * @return Подтверждение успешного удаления изображения.
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Remove drive directory picture",
            description = "Removes a drive directory picture from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Drive directory picture not found")
            })
    public ResponseEntity<Void> deleteDriveDirPicture(@PathVariable Long id) {
        driveDirPictureService.deleteDriveDirPicture(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
