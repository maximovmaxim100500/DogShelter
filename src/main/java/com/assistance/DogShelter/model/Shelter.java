package com.assistance.DogShelter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Модель, представляющая приют.
 */
@Entity
@Table(name = "shelters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Pet> pets;      // связать с классом Pet
}
