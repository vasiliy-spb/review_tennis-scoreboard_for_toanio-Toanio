package com.example.tennis.scoreboard.exception;

public class PlayerAlreadyExistsException extends RuntimeException {

    // Шаблон сообщения для этого исключения можно хранить в виде константы в самом классе исключения,
        // а в конструктор получать только имя игрока.

    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}
