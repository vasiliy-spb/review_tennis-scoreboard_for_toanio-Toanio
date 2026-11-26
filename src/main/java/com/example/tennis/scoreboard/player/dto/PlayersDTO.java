package com.example.tennis.scoreboard.player.dto;

public record PlayersDTO(

        // Если в DTO передаются данные одного игрока, то в названии стоит использовать единственное число: Player*.

        Integer id,
        String name
) {
}
