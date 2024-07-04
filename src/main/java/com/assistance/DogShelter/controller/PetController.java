package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.model.Pet;
import com.assistance.DogShelter.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с питомцем.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    /**
     * Конструктор контроллера, который принимает сервис для работы с питомцем.
     *
     * @param petService Сервис для работы с питомцем.
     */
    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового питомца.
     *
     * @param pet Переданный питомец в теле запроса.
     * @return Питомец, который был добавлен с помощью сервиса.
     */
    @PostMapping("/add")
    @Operation(summary = "Add pet",
            description = "Adds a new pet to the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        Pet addPet = petService.addPet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(addPet);
    }

    /**
     * Обрабатывает GET-запрос для поиска питомца по идентификатору.
     *
     * @param id Идентификатор питомца.
     * @return Питомец с указанным идентификатором, если найден, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    @Operation(summary = "Search pet",
            description = "Searches for a pet in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Pet not found")
            })
    public ResponseEntity<Pet> findPetById(@PathVariable Long id) {
        Optional<Pet> pet = petService.findPetById(id);
        return pet.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Обрабатывает PUT-запрос для редактирования питомца.
     *
     * @param pet Питомец с обновленными данными в теле запроса.
     * @param id  Идентификатор редактируемого питомца.
     * @return Обновленный питомец, если редактирование выполнено успешно, в противном случаи возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    @Operation(summary = "Update pet",
            description = "Updates a pet's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Pet not found")
            })
    public ResponseEntity<Pet> editPet(@RequestBody Pet pet, @PathVariable Long id) {
        Optional<Pet> foundPet = petService.findPetById(id);
        if (foundPet.isPresent()) {
            Pet updatedPet = petService.editPet(pet);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPet);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления питомца по его идентификатору.
     *
     * @param id Идентификатор питомца для удаления.
     * @return Подтверждение успешного удаления питомца.
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Remove pet",
            description = "Removes a pet from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Pet not found")
            })
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        if (petService.findPetById(id).isPresent()) {
            petService.deletePet(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает GET-запрос для получения всех питомцев.
     *
     * @return Список всех питомцев в системе.
     */
    @GetMapping("/all")
    @Operation(summary = "Get all pets",
            description = "Retrieves a list of all pets in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<Collection<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }
}