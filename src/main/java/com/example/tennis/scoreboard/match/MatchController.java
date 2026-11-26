package com.example.tennis.scoreboard.match;

import com.example.tennis.scoreboard.match.dto.MatchDTORequest;
import com.example.tennis.scoreboard.match.dto.MatchDTOResponse;
import com.example.tennis.scoreboard.match.dto.MatchInfoDTO;
import com.example.tennis.scoreboard.match.dto.MatchesDTO;
import com.example.tennis.scoreboard.player.dto.PlayersDTORequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/matches")
@Validated
public class MatchController {

    // Вместо "магических чисел" 201, 200 стоит использовать константы HttpStatus
        // или фабричные методы ResponseEntity.ok() и ResponseEntity.created().

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<MatchDTOResponse> createMatch(
            @RequestBody @Valid MatchDTORequest request
    ) {

        // TODO: Контроллер не должен заниматься сборкой DTO для ответа — это ответственность сервисного слоя или маппера.
            // Лучше, чтобы matchService.createMatch(request) сразу возвращал готовый DTO-ответ.
            // (см. файл "fat-controller.md" в этом же пакете)
        // Вызовы методов builder-а удобнее читать, если они расположены на отдельных строках:
            // ResponseEntity.status(201)
            //    .body(new MatchDTOResponse(matchService.createMatch(request)));
        // Код с промежуточными переменными с понятными именами читается лучше,
            // чем вызовы методов, передаваемые в аргументы другим методам.
        return ResponseEntity.status(201).body(new MatchDTOResponse(matchService.createMatch(request)));
    }

    // Имя метода лучше согласовать с бизнес-действием: addPoint вместо setPoint.
    @PostMapping("/{uuid}/point")
    public ResponseEntity<MatchInfoDTO> setPoint(
            @PathVariable UUID uuid,
            @RequestBody @Valid PlayersDTORequest request
    ) {

        // Вызовы методов builder-а удобнее читать, если они расположены на отдельных строках:
            // ResponseEntity.status(200)
            //    .body(matchService.setPoint(uuid, request.name()));
        // Код с промежуточными переменными с понятными именами читается лучше,
            // чем вызовы методов, передаваемые в аргументы другим методам.
        return ResponseEntity.status(200).body(matchService.setPoint(uuid, request.name()));
    }

    @GetMapping
    public ResponseEntity<MatchesDTO> getFinishedMatches(
            @RequestParam(required = false, name = "player_name")

            // Поисковый запрос можно не валидировать, чтобы клиент получал не ошибку валидации, а пустой результат поиска.
            @Size(min = 2, max = 30, message = "Player name must be between 2 and 30 characters")
            @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9]+$", message = "Player name must not contain special characters")
            String playerName,

            // TODO: На параметре Pageable не указана аннотация @SortDefault (или @PageableDefault(sort = ...)).
                // И в запросе в репозитории тоже отсутствует ORDER BY.
                // Если клиент не передаст параметр sort в URL, Spring инициализирует Pageable со значением Sort.unsorted()
                // и порядок записей будет недетерминированным.
                // При переходе на вторую страницу пользователь может увидеть дубликаты матчей с первой страницы или пропустить некоторые записи.
            Pageable pageable
    ) {

        // Вызовы методов builder-а удобнее читать, если они расположены на отдельных строках:
            // ResponseEntity.status(200)
            //    .body(matchService.allFinishMatches(playerName, pageable));
        // Код с промежуточными переменными с понятными именами читается лучше,
            // чем вызовы методов, передаваемые в аргументы другим методам.
        return ResponseEntity.status(200).body(matchService.allFinishMatches(playerName, pageable));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MatchInfoDTO> getMatchScore(
            @PathVariable UUID uuid
    ) {

        // Вызовы методов builder-а удобнее читать, если они расположены на отдельных строках:
            // ResponseEntity.status(200)
            //    .body(matchService.getMatchScore(uuid));
        // Код с промежуточными переменными с понятными именами читается лучше,
            // чем вызовы методов, передаваемые в аргументы другим методам.
        return ResponseEntity.status(200).body(matchService.getMatchScore(uuid));
    }

}
