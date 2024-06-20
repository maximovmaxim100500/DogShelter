package com.assistance.DogShelter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "drive_dir_picture")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriveDirPicture {
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
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;
}
