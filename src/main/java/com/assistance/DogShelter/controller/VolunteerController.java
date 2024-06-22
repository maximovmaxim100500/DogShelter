package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.model.User;
import com.assistance.DogShelter.model.Volunteer;
import com.assistance.DogShelter.service.VolunteerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с питомцами.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/volunteer")
public class VolunteerController {


    private final VolunteerService volunteerService;

    /**
     * Конструктор контроллера, который принимает сервис для работы с волонтерами.
     *
     * @param volunteersService Сервис для работы с волонтерами.
     */
    public VolunteerController(VolunteerService volunteersService) {
        this.volunteerService = volunteersService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового волонтера.
     *
     * @param volunteer Переданный волонтер в теле запроса.
     * @return волонтер , который был добавлен с помощью сервиса.
     */
    @PostMapping("/add")
    public Volunteer addVolunteer(@RequestBody Volunteer volunteer) {
        Volunteer addVolunteer = volunteerService.addVolunteer(volunteer);
        return ResponseEntity.status(HttpStatus.CREATED).body(addVolunteer).getBody();
    }

    /**
     * Обрабатывает GET-запрос для поиска волонтера по идентификатору.
     *
     * @param id Идентификатор волонтера.
     * @return волонтер с указанным идентификатором, если найден, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    public Volunteer findVolunteerById(@PathVariable Long id) {
        Optional<Volunteer> volunteer = volunteerService.findVolunteerById(id);

        if (volunteer.isEmpty()) {
            return (Volunteer) ResponseEntity.status(HttpStatus.NOT_FOUND).build().getBody();
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(findVolunteerById(id)).getBody();
    }

    /**
     * Обрабатывает PUT-зарос для редактирования волонтера.
     *
     * @param volunteer волонтерами с обновленными данными в теле запроса.
     * @param id        Идентификатор редактируемого волонтера.
     * @return Обновленный волонтер, если редактирование выполнено успешно, в противном случае возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    public Volunteer editVolunteer(@RequestBody Volunteer volunteer, @PathVariable Long id) {
        Volunteer foundVolunteer = volunteerService.editVolunteer(volunteer);

        if (foundVolunteer == null) {
            return (Volunteer) ResponseEntity.status(HttpStatus.NOT_FOUND).build().getBody();
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(findVolunteerById(id)).getBody();
    }

    /**
     * Обрабатывает POST-запрос для изменения статуса занятости волонтера
     *
     * @param userId Идентификатор волонтера для установления занятости
     * @param busy   Идентификатор для установки занятости (<code>true</code> - занят, <code>false</code> - не занят)
     * @return Подтверждение успешного изменения статуса занятости волонтера
     */
    @PostMapping("{userId}")
    public Volunteer setVolunteerBusy(@PathVariable Long userId, Boolean busy) {
        Volunteer setVolunteer = volunteerService.keepVolunteerBusy(userId, busy);
        return ResponseEntity.status(HttpStatus.FOUND).body(setVolunteer).getBody();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления волонтера
     *
     * @param id Идентификатор волонтера для удаления
     * @return Подтверждение успешного удаления волонтера
     */
    @DeleteMapping("{id}")
    public Volunteer deleteVolunteer(@PathVariable Long id) {
        volunteerService.deleteVolunteer(id);
        return (Volunteer) ResponseEntity.status(HttpStatus.OK);
    }

//    /**
//     * Обрабатывает DELETE-запрос для удаления волонтера
//     *
//     * @param chatId Идентификатор Телеграм волонтера для удаления
//     * @return Подтверждение успешного удаления волонтера
//     */
//    @DeleteMapping("{chatId}")
//    public Volunteer deleteVolunteerChatId(@PathVariable Long chatId) {
//        volunteerService.deleteVolunteerChatId(chatId);
//        return (Volunteer) ResponseEntity.status(HttpStatus.OK);
//    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Volunteer>> getAllVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllVolunteers());
    }

}
