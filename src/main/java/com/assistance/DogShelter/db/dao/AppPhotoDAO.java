package com.assistance.DogShelter.db.dao;

import com.assistance.DogShelter.db.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
}