package com.assistance.DogShelter.repositories;

import com.assistance.DogShelter.model.User;
import com.assistance.DogShelter.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет пользователя по идентификатору чата.
     *
     * @param chatId идентификатор чата
     * @return Optional, содержащий пользователя, если найден
     */
    Optional<User> findByChatId(long chatId);

    User findById(long id);
}
