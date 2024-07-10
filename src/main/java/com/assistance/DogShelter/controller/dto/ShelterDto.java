package com.assistance.DogShelter.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class ShelterDto {
    private long id;
    private String name;
    private String address;
    private List<PetDto> pets;// связать с классом Pet
}
