package com.example.tennis.scoreboard.match.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MatchDTORequest(

        // Суффикс Request уже подразумевает, что это объект передачи данных (DTO) для входящего запроса,
                // поэтому использовать оба слова вместе избыточно.

        // В аннотации @Size можно использовать плейсхолдеры в фигурных скобках:
                // "Name must be between {min} and {max} characters",
                // тогда соответствующие значения подставятся автоматически.

        // Можно назвать CreateMatchRequest.

        @NotBlank(message = "Name cannot be blank")

        // Сообщения можно сделать точнее: "First player name ..."
        @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9]+$", message = "Name must not contain special characters")
        String firstPlayerName,

        @NotBlank(message = "Name cannot be blank")

        // Сообщения можно сделать точнее: "Second player name ..."
        @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9]+$", message = "Name must not contain special characters")
        String secondPlayerName
) {
}
