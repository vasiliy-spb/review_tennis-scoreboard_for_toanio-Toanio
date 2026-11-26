package com.example.tennis.scoreboard.match.dto;

import java.util.UUID;

public record MatchDTOResponse(

        // Суффикс Response уже подразумевает, что это объект передачи данных (DTO) для ответа,
                // поэтому использовать оба слова вместе избыточно.

        UUID id
) {
}
