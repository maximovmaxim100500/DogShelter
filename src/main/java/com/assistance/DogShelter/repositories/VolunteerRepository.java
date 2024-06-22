package com.assistance.DogShelter.repositories;

import com.assistance.DogShelter.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findByChatId(long chatId);
    Optional<Volunteer> findFirstByIsBusy(boolean bool);

}
