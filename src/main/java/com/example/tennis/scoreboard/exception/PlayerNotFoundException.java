package com.example.tennis.scoreboard.exception;

public class PlayerNotFoundException extends RuntimeException {

    // Шаблон сообщения для этого исключения можно хранить в виде константы в самом классе исключения,
        // а в конструктор получать только имя игрока.

    public PlayerNotFoundException(String message) {
        super(message);
    }
}
