package com.assistance.DogShelter.controller;
import com.assistance.DogShelter.model.PetAvatar;
import com.assistance.DogShelter.service.PetAvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с аватаром питомца.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/petAvatars")
public class PetAvatarController {

    private final PetAvatarService petAvatarService;

    public PetAvatarController(PetAvatarService petAvatarService) {
        this.petAvatarService = petAvatarService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового аватара питомца.
     *
     * @param petAvatar Переданный аватар в теле запроса.
     * @return Аватар, который был добавлен с помощью сервиса.
     */
    @PostMapping("/add")
    @Operation(summary = "Add pet avatar",
            description = "Adds a new pet avatar to the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<PetAvatar> addPetAvatar(@RequestBody PetAvatar petAvatar) {
        PetAvatar addPetAvatar = petAvatarService.addPetAvatar(petAvatar);
        return ResponseEntity.status(HttpStatus.CREATED).body(addPetAvatar);
    }

    /**
     * Обрабатывает GET-запрос для поиска аватара по идентификатору.
     *
     * @param id Идентификатор аватара.
     * @return Аватар с указанным идентификатором, если найден, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    @Operation(summary = "Search pet avatar",
            description = "Searches for a pet avatar in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Pet avatar not found")
            })
    public ResponseEntity<PetAvatar> findPetAvatarById(@PathVariable Long id) {
        Optional<PetAvatar> petAvatar = petAvatarService.findPetAvatarById(id);
        return petAvatar.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Обрабатывает PUT-запрос для редактирования аватара питомца.
     *
     * @param petAvatar Аватар с обновленными данными в теле запроса.
     * @param id        Идентификатор редактируемого аватара.
     * @return Обновленный аватар, если редактирование выполнено успешно, в противном случаи возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    @Operation(summary = "Update pet avatar",
            description = "Updates a pet avatar's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Pet avatar not found")
            })
    public ResponseEntity<PetAvatar> editPetAvatar(@RequestBody PetAvatar petAvatar, @PathVariable Long id) {
        Optional<PetAvatar> foundPetAvatar = petAvatarService.findPetAvatarById(id);
        if (foundPetAvatar.isPresent()) {
            PetAvatar updatedPetAvatar = petAvatarService.editPetAvatar(petAvatar);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPetAvatar);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления аватара по его идентификатору.
     *
     * @param id Идентификатор аватара для удаления.
     * @return Подтверждение успешного удаления аватара.
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Remove pet avatar",
            description = "Removes a pet avatar from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Pet avatar not found")
            })
    public ResponseEntity<Void> deletePetAvatar(@PathVariable Long id) {
        if (petAvatarService.findPetAvatarById(id).isPresent()) {
            petAvatarService.deletePetAvatar(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
