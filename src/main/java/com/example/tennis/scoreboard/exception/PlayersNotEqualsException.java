package com.example.tennis.scoreboard.exception;

public class PlayersNotEqualsException extends RuntimeException {

    // Сообщение для этого исключения можно хранить в виде константы в самом классе исключения.

    public PlayersNotEqualsException(String message) {
        super(message);
    }
}
