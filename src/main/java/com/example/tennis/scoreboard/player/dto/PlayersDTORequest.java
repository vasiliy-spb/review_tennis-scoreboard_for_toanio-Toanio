package com.example.tennis.scoreboard.player.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PlayersDTORequest(

        // Суффикс Request уже подразумевает, что это объект передачи данных (DTO) для входящего запроса,
                // поэтому использовать оба слова вместе избыточно.

        // Если в теле запроса передаются данные одного игрока, то в названии стоит использовать единственное число: Player*.

        // Можно назвать CreatePlayerRequest.

        // Реальные имена могут содержать дефисы, точки и апострофы. А также было бы уместно разрешить букву ё.
                // А также можно запретить цифры.

        @NotBlank(message = "Name cannot be blank")

        // В аннотации @Size можно использовать плейсхолдеры в фигурных скобках:
                // "Name must be between {min} and {max} characters",
                // тогда соответствующие значения подставятся автоматически.
        @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")

        // Для сообщения к такому строгому паттеру больше подошло бы описание того, что МОЖНО использовать в имени.
        @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9]+$", message = "Name must not contain special characters")
        String name
) {
}
