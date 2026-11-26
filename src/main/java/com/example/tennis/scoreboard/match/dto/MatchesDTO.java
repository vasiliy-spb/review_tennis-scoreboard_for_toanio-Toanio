package com.example.tennis.scoreboard.match.dto;

import java.util.List;

public record MatchesDTO(

        // Поля currentPage и totalPages не могут быть null, поэтому для них лучше использовать примитивный тип int вместо обёртки.

        // Можно назвать точнее — например, MatchesPageDto.

        List<FinishedMatchDTO> matches,
        Integer currentPage,
        Integer totalPages
) {
}
