package com.assistance.DogShelter.model;

import com.assistance.DogShelter.enums.Food;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;            // id питомца

    @Column(name = "name", nullable = false)
    private String name;        // имя питомца

    @Column(name = "breed", nullable = false)
    private String breed;       // порода питомца

    @Column(name = "age", nullable = false)
    private int age;            // возраст питомца

    @Column(name = "food")
    private String food;        // предпочитаемая еда питомца

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("pets")
    private User user;          // связать с классом User
}