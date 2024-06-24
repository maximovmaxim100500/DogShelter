package com.assistance.DogShelter.repositories;

import com.assistance.DogShelter.model.User;
import com.assistance.DogShelter.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(long chatId);
}
