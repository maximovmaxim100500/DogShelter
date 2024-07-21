package com.assistance.DogShelter.db.dao;

import com.assistance.DogShelter.db.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
}
