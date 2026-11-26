package com.example.tennis.scoreboard.player;

import com.example.tennis.scoreboard.player.dto.PlayersDTO;
import com.example.tennis.scoreboard.player.dto.PlayersDTORequest;
import com.example.tennis.scoreboard.exception.MessageDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    // Эндпоинты этого контроллера не предусмотрены ТЗ. Но если они созданы, то было бы уместно сделать для них страницу на фронтенде.

    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @PostMapping
    // TODO: Эндпоинт создания игрока должен возвращать данные созданной сущности, а сообщение о создании.
    public ResponseEntity<MessageDto> createPlayers(
            @RequestBody @Valid PlayersDTORequest request
    ) {
        // Вместо магического числа 201 можно использовать ResponseEntity.created() или ResponseEntity.status(HttpStatus.CREATED).
        // Вызовы методов builder-а удобнее читать, если они расположены на отдельных строках:
            // ResponseEntity.status(201)
            //     .body(service.createPlayer(request));
        // Код с промежуточными переменными с понятными именами читается лучше,
            // чем вызовы методов, передаваемые в аргументы другим методам.
        return ResponseEntity.status(201).body(service.createPlayer(request));
    }

    @GetMapping
    // В названии больше подошёл бы глагол get или find вместо show.
    public List<PlayersDTO> showAllPlayers(

            // TODO: На параметре Pageable не указана аннотация @SortDefault (или @PageableDefault(sort = ...)).
                // Если клиент не передаст параметр sort в URL, Spring инициализирует Pageable со значением Sort.unsorted()
                // и порядок записей будет недетерминированным.
                // При переходе на вторую страницу пользователь может увидеть дубликаты матчей с первой страницы или пропустить некоторые записи.
            Pageable pageable
    ) {
        return service.showAllPlayers(pageable);
    }

}
