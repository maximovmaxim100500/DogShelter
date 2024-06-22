package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.model.User;
import com.assistance.DogShelter.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с пользователем.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * Конструктор контроллера, который принимает сервис для работы с пользователем.
     *
     * @param userService Сервис для работы с пользователем.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового пользователя.
     *
     * @param user Переданный пользователь в теле запроса.
     * @return Пользователь, который был добавлен с помощью сервиса.
     */
    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        User addUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(addUser).getBody();
    }

    /**
     * Обрабатывает GET-запрос для поиска пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Пользователь с указанным идентификатором, если найден, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    public User findUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isEmpty()) {
            return (User) ResponseEntity.status(HttpStatus.NOT_FOUND).build().getBody();
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(findUserById(id)).getBody();
    }

    /**
     * Обрабатывает PUT-зарос для редактирования пользователя.
     *
     * @param user Пользователь с обновленными данными в теле запроса.
     * @param id   Идентификатор редактируемого пользователя.
     * @return Обновленный пользователь, если редактирование выполнено успешно, в противном случаи возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    public User editUser(@RequestBody User user, @PathVariable Long id) {
        User foundUser = userService.editUser(user);
        if (foundUser == null) {
            return (User) ResponseEntity.status(HttpStatus.NOT_FOUND).build().getBody();
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(findUserById(id)).getBody();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     * @return Подтверждение успешного удаления пользователя.
     */
    @DeleteMapping("{id}")
    public User deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return (User) ResponseEntity.status(HttpStatus.OK);
    }

//    /**
//     * Обрабатывает DELETE-запрос для удаления пользователя по его идентификатору Телеграм
//     *
//     * @param chatId Идентификатор Телеграм волонтера для удаления
//     * @return Подтверждение успешного удаления волонтера
//     */
//    @DeleteMapping("{chatId}")
//    public User deleteUserChatId(@PathVariable Long chatId) {
//        userService.deleteUserChatId(chatId);
//        return (User) ResponseEntity.status(HttpStatus.OK);
//    }

    @GetMapping("/all")
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
