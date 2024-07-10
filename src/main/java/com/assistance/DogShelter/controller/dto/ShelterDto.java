package com.assistance.DogShelter.controller.dto;

import com.assistance.DogShelter.db.model.Pet;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;
@Data
@Builder
public class ShelterDto {
    private long id;
    private String name;
    private String address;
    private List<PetDto> pets;// связать с классом Pet
}
