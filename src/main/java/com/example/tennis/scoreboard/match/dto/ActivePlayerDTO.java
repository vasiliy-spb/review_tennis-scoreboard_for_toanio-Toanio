package com.example.tennis.scoreboard.match.dto;

public record ActivePlayerDTO(

        // Класс хранит данные не только об игроке, но и о его счёте, поэтому больше подошло бы название, например, PlayerScoreDto.

        // Поля games и sets не могут быть null, поэтому для них лучше использовать примитивный тип int вместо обёртки.

        String name,
        String points,
        Integer games,
        Integer sets,
        Integer tieBreakPoints
) {
}
