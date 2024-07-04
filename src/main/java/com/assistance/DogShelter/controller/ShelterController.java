package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.model.Shelter;
import com.assistance.DogShelter.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с приютами.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/shelters")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового приюта.
     *
     * @param shelter Переданный приют в теле запроса.
     * @return Приют, который был добавлен с помощью сервиса.
     */
    @PostMapping("/add")
    @Operation(summary = "Add shelter",
            description = "Adds a new shelter to the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<Shelter> addShelter(@RequestBody Shelter shelter) {
        Shelter addedShelter = shelterService.addShelter(shelter);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedShelter);
    }

    /**
     * Обрабатывает GET-запрос для поиска приюта по идентификатору.
     *
     * @param id Идентификатор приюта.
     * @return Приют с указанным идентификатором, если найден, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    @Operation(summary = "Search shelter",
            description = "Searches for a shelter in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Shelter not found")
            })
    public ResponseEntity<Shelter> findShelterById(@PathVariable Long id) {
        Optional<Shelter> shelter = shelterService.findShelterById(id);
        return shelter.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Обрабатывает PUT-запрос для редактирования приюта.
     *
     * @param shelter Приют с обновленными данными в теле запроса.
     * @param id      Идентификатор редактируемого приюта.
     * @return Обновленный приют, если редактирование выполнено успешно, в противном случае возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    @Operation(summary = "Update shelter",
            description = "Updates a shelter's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Shelter not found")
            })
    public ResponseEntity<Shelter> editShelter(@RequestBody Shelter shelter, @PathVariable Long id) {
        Optional<Shelter> foundShelter = shelterService.findShelterById(id);
        if (foundShelter.isPresent()) {
            Shelter updatedShelter = shelterService.editShelter(shelter);
            return ResponseEntity.status(HttpStatus.OK).body(updatedShelter);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления приюта по его идентификатору.
     *
     * @param id Идентификатор приюта для удаления.
     * @return Подтверждение успешного удаления приюта.
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Remove shelter",
            description = "Removes a shelter from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Shelter not found")
            })
    public ResponseEntity<Void> deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Обрабатывает GET-запрос для получения списка всех приютов.
     *
     * @return Список всех приютов в системе.
     */
    @GetMapping("/all")
    @Operation(summary = "List all shelters",
            description = "Retrieves a list of all shelters in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public List<Shelter> getAllShelters() {
        return shelterService.getAllShelters();
    }
}
