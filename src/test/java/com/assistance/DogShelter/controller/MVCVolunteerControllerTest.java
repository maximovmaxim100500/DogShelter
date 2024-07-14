package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.db.model.Volunteer;
import com.assistance.DogShelter.service.VolunteerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(VolunteerController.class)
class MVCVolunteerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerService volunteerService;
    @Autowired
    private Volunteer volunteer;

    @BeforeEach
    void setUp() {
        volunteer = new Volunteer();
        volunteer.setId(1L);
        volunteer.setName("John Doe");
    }

    @Test
    void testAddVolunteer() throws Exception {
        Mockito.when(volunteerService.addVolunteer(any(Volunteer.class))).thenReturn(volunteer);

        mockMvc.perform(MockMvcRequestBuilders.post("/volunteers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testFindVolunteerById() throws Exception {
        Mockito.when(volunteerService.findVolunteerById(anyLong())).thenReturn(Optional.of(volunteer));

        mockMvc.perform(MockMvcRequestBuilders.get("/volunteers/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testEditVolunteer() throws Exception {
        Mockito.when(volunteerService.findVolunteerById(anyLong())).thenReturn(Optional.of(volunteer));
        Mockito.when(volunteerService.editVolunteer(any(Volunteer.class))).thenReturn(volunteer);

        mockMvc.perform(MockMvcRequestBuilders.put("/volunteers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testSetVolunteerBusy() throws Exception {
        Mockito.when(volunteerService.findVolunteerById(anyLong())).thenReturn(Optional.of(volunteer));
        Mockito.when(volunteerService.keepVolunteerBusy(anyLong(), any(Boolean.class))).thenReturn(volunteer);

        mockMvc.perform(MockMvcRequestBuilders.post("/volunteers/1")
                        .param("busy", "true"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testDeleteVolunteer() throws Exception {
        Mockito.when(volunteerService.findVolunteerById(anyLong())).thenReturn(Optional.of(volunteer));

        mockMvc.perform(MockMvcRequestBuilders.delete("/volunteers/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteVolunteerByChatId() throws Exception {
        Mockito.when(volunteerService.findVolunteerByChatId(anyLong())).thenReturn(Optional.of(volunteer));

        mockMvc.perform(MockMvcRequestBuilders.delete("/volunteers/chatId/12345"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllVolunteers() throws Exception {
        Mockito.when(volunteerService.getAllVolunteers()).thenReturn(Arrays.asList(volunteer));

        mockMvc.perform(MockMvcRequestBuilders.get("/volunteers/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John Doe"));
    }
}