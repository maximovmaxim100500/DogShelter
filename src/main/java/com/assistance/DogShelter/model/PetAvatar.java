package com.assistance.DogShelter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Модель для представления аватара питомца.
 */
@Entity
@Table(name = "pet_avatars")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetAvatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;
    @Column(nullable = false)
    private String filePath;
    @Column(nullable = false)
    private long fileSize;
    @Column(nullable = false)
    private String mediaType;
    @Lob
    @Column(nullable = false)
    private byte[] data;
    @OneToOne
    @JoinColumn(name = "pet_id", nullable = false)
    Pet pet;
}
