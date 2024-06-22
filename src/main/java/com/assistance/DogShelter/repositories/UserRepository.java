package com.assistance.DogShelter.repositories;

import com.assistance.DogShelter.model.User;
import com.assistance.DogShelter.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(long chatId);
}
