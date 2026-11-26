package com.example.tennis.scoreboard.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Вместо "магических чисел" лучше использовать константы HttpStatus или фабричные методы ResponseEntity.

    // TODO: MethodArgumentTypeMismatchException возникает при неверном типе любого параметра (например, page=abc),
        // а не только UUID. Жёстко зашитое сообщение "Invalid UUID" будет вводить в заблуждение.
        // Лучше формировать сообщение динамически, используя имя параметра ex.getName().
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MessageDto> handleWrongUUID(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(400).body(new MessageDto("Invalid UUID"));
    }

    // Обрабатывается MatchNotFoundException, а метод называется handlePlayerAlreadyExists.
    // Обработчики для MatchNotFoundException и PlayerNotFoundException полностью дублируют друг друга.
        // Их можно объединить в один метод так:
        // @ExceptionHandler({MatchNotFoundException.class, PlayerNotFoundException.class}).
    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<MessageDto> handlePlayerAlreadyExists(MatchNotFoundException ex) {
        return ResponseEntity.status(404).body(new MessageDto(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDto> handleNotValidArgument(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        // Метод error.getCode() может вернуть null и вызов .equals() приведёт к NullPointerException.
            // Безопаснее вызывать "NotBlank".equals(error.getCode()).
        // Список fieldErrors может оказаться пустым и тогда fieldErrors.get(0) упадёт с IndexOutOfBoundsException.
            // Надёжнее использовать ex.getBindingResult().getAllErrors().stream().findFirst().
            // Ещё лучше — возвращать клиенту сразу весь список ошибок. Это улучшит пользовательский опыт.
        String errorMessage = fieldErrors.stream()
                .filter(error -> error.getCode().equals("NotBlank") || error.getCode().equals("NotNull"))
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElseGet(() -> fieldErrors.get(0).getDefaultMessage());
        return ResponseEntity.status(400).body(new MessageDto(errorMessage));
    }

    // Этот обработчик и handlePlayerNotEquals полностью идентичны. Их тоже можно объединить в один метод.
    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public ResponseEntity<MessageDto> handlePlayerAlreadyExists(PlayerAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(new MessageDto(ex.getMessage()));
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<MessageDto> handlePlayerNotFound(PlayerNotFoundException ex) {
        return ResponseEntity.status(404).body(new MessageDto(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessageDto> handleConstraintViolation(ConstraintViolationException ex) {

        // Вызов .iterator().next() без проверки на пустоту может привести к NoSuchElementException.
            // Безопаснее использовать .stream().findFirst().map(ConstraintViolation::getMessage).orElse("Validation error").
            // И здесь тоже лучше возвращать клиенту сразу весь список ошибок. Это улучшит пользовательский опыт.
        String errorMessage = ex.getConstraintViolations()
                .iterator()
                .next()
                .getMessage();

        return ResponseEntity.status(400)
                .body(new MessageDto(errorMessage));
    }

    // Опечатка: handlePlayerNotEquals -> handlePlayersNotEquals.
    @ExceptionHandler(PlayersNotEqualsException.class)
    public ResponseEntity<MessageDto> handlePlayerNotEquals(PlayersNotEqualsException ex) {
        return ResponseEntity.status(409).body(new MessageDto(ex.getMessage()));
    }

}
