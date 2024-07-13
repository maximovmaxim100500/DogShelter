package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.controller.dto.PetDto;
;
import com.assistance.DogShelter.db.repository.PetRepository;
import com.assistance.DogShelter.service.PetService;
import jakarta.ws.rs.core.MediaType;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class MVCPetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetRepository petRepository;
    @SpyBean
    private PetService petService;

    @InjectMocks
    private PetController petController;

    @Test
    public void getPetByIdTest() throws Exception {
        Long id = 1L;
        String name = "Дружок";
        String breed = "Лабрадор";
        int age = 3;
        String food = "Сухой корм";

        PetDto expectedPet = new PetDto();
        expectedPet.setId(id);
        expectedPet.setName(name);
        expectedPet.setBreed(breed);
        expectedPet.setAge(age);
        expectedPet.setFood(food);

        when(petService.findPetById(id)).thenReturn(Optional.of(expectedPet));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value(breed))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age))
                .andExpect(MockMvcResultMatchers.jsonPath("$.food").value(food));
    }

    @Test
    void getAllPetsTest() throws Exception {
        // Создаем и сохраняем питомцев в сервисе или базе данных
        List<PetDto> pets = Arrays.asList(
                new PetDto(1, "Рекс", "Лабрадор", 3, "Сухой корм", null),
                new PetDto(2, "Вискас", "Немецкая овчарка", 2, "Сухой корм", null)
        );
        when(petService.getAllPets()).thenReturn(pets);

        mockMvc.perform(get("/pets/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2))); // Проверяем, что вернулась коллекция из двух питомцев
    }

    @Test
    @Disabled
    public void createPetTest() throws Exception {
        Long id = 1L;
        String name = "Дружок";
        String breed = "Лабрадор";
        int age = 3;
        String food = "Сухой корм";

        JSONObject petObject = new JSONObject();
        petObject.put("id", id);
        petObject.put("name", name);
        petObject.put("age", age);
        petObject.put("food", food);

        PetDto newPet = new PetDto();
        newPet.setId(id);
        newPet.setName(name);
        newPet.setBreed(breed);
        newPet.setAge(age);
        newPet.setFood(food);

//        when()

//        when(petService.addPet(any(PetDto.class))).thenReturn(newPet);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/pets")
//                        .content("{\"name\":\"Дружок\",\"breed\":\"Лабрадор\",\"age\":3,\"food\":\"Сухой корм\"}")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value(breed))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.food").value(food));
    }

    @Test
    public void deletePetTest() throws Exception {
        Long id = 1L;
        String name = "Дружок";
        String breed = "Лабрадор";
        int age = 3;
        String food = "Сухой корм";

        PetDto petToDelete = new PetDto();
        petToDelete.setId(id);
        petToDelete.setName(name);
        petToDelete.setBreed(breed);
        petToDelete.setAge(age);
        petToDelete.setFood(food);

        doNothing().when(petService).deletePet(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findPetByIdTest() throws Exception {
        Long id = 1L;
        String name = "Дружок";
        String breed = "Лабрадор";
        int age = 3;

        PetDto expectedPet = new PetDto();
        expectedPet.setId(id);
        expectedPet.setName(name);
        expectedPet.setBreed(breed);
        expectedPet.setAge(age);

        mockMvc.perform(get("/pets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue())); // Проверка на соответствие идентификатора питомца
    }
}