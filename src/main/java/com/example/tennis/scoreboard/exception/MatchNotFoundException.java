package com.example.tennis.scoreboard.exception;

public class MatchNotFoundException extends RuntimeException {

    // Шаблон сообщения для этого исключения можно хранить в виде константы в самом классе исключения,
        // а в конструктор получать ID матча.

    public MatchNotFoundException(String message) {
        super(message);
    }
}
