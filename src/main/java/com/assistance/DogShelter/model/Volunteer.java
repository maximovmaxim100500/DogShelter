package com.assistance.DogShelter.model;

// Модель, представляющая волонтера

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "volunteers", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;              // id волонтера
    @Column(name = "chat_id", nullable = false)
    private long chatId;          // id волонтера в Telegram
    @Column(name = "name", nullable = false)
    private String name;          // имя волонтера
   @Column(name = "is_busy", nullable = false)
    private boolean isBusy;       //статус волонтёра (занят)
}
