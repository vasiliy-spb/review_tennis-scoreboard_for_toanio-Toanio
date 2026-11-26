package com.example.tennis.scoreboard.match;

import com.example.tennis.scoreboard.match.dto.*;
import com.example.tennis.scoreboard.player.Player;
import com.example.tennis.scoreboard.player.PlayerService;
import com.example.tennis.scoreboard.exception.MatchNotFoundException;
import com.example.tennis.scoreboard.exception.PlayerNotFoundException;
import com.example.tennis.scoreboard.exception.PlayersNotEqualsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MatchService {

    // TODO: Класс совмещает несколько разных ответственностей:
        // - работает с текущими матчами
        // - работает с завершёнными матчами
        // - управляет хранилищем активных матчей в памяти
        //
        // Это нарушает принцип единой ответственности (SRP).
        // Стоит разделить эти роли на несколько более специализированных классов.

    private final PlayerService playerService;

    // Стоит использовать классы через их интерфейсы — Map<UUID, ActiveMatch> matchSession = new ConcurrentHashMap<>();
    // Map с матчами лучше назвать во множественном числе.
    // У слова сессия есть конкретные значения в контексте бэкенда, поэтому для хранилища матчей стоит подобрать более подходящее название.
    private final ConcurrentHashMap<UUID, ActiveMatch> matchSession = new ConcurrentHashMap<>();
    private final MatchMapper matchMapper;
    private final MatchRepository matchRepository;

    public MatchService(PlayerService playerService, MatchMapper matchMapper, MatchRepository matchRepository) {
        this.playerService = playerService;
        this.matchMapper = matchMapper;
        this.matchRepository = matchRepository;
    }


    public UUID createMatch(MatchDTORequest request) {
        String firstName = request.firstPlayerName().trim();
        String secondName = request.secondPlayerName().trim();

        // TODO: Проверка на совпадение имён должна учитывать регистр (equalsIgnoreCase),
            // иначе можно создать матч между "Петя" и "петя", хотя в репозитории поиск игроков регистронезависимый.
        if (firstName.equals(secondName)) {

            // TODO: Название исключения PlayersNotEqualsException противоречит его смыслу —
                // оно выбрасывается, когда игроки как раз совпадают.
                // Неправильные исключения вводят в заблуждение и усложняют отладку кода.
                // Лучше переименовать например в DuplicatePlayerException.
            throw new PlayersNotEqualsException("Players name cannot be equals");
        }

        // TODO: Здесь используются сырые строки из request, хотя в начале метода пробелы уже были обрезаны в firstName и secondName.
            // Нужно передавать уже очищенные переменные, иначе пробелы по краям помешают найти игрока.
        Player player1 = playerService.findPlayerByName(request.firstPlayerName());
        Player player2 = playerService.findPlayerByName(request.secondPlayerName());

        if (player1 == null || player2 == null) {

            // Это исключение можно бросать в методе findPlayerByName в PlayerService.
                // А также передавать в него имя для более удобной отладки.
            throw new PlayerNotFoundException("Player not found");
        }

        // Лучше matchId
        UUID uuid = UUID.randomUUID();

        // Объекты моделей, которые используются в классах других моделей,
            // должны создаваться внутри самого слоя моделей, а не в сервисе.
        ActivePlayer activePlayer1 = new ActivePlayer(player1.getName());
        ActivePlayer activePlayer2 = new ActivePlayer(player2.getName());

        matchSession.put(uuid, new ActiveMatch(activePlayer1, activePlayer2));

        return uuid;
    }

    // Имя метода setPoint вводит в заблуждение — префикс `set` обычно используется для сеттеров,
        // а здесь происходит начисление очка и обновление состояния матча. Лучше назвать addPoint.
    public MatchInfoDTO setPoint(UUID uuid, String name) {
        Player player = playerService.findPlayerByName(name);
        if (player == null) {

            // Пропущен пробел перед "not found" — имя игрока склеится со следующим текстом.
            throw new PlayerNotFoundException("Player " + name + "not found");

        }
        ActiveMatch match = getActiveMatch(uuid);

        // TODO: Использование ConcurrentHashMap защищает только саму хэш-таблицу, но не состояние объекта ActiveMatch внутри неё.
            // При одновременных запросах на начисление очка может возникнуть состояние гонки (race condition).
            // Например, очки посчитаются некорректно или матч сохранится в БД дважды.
            // Стоит сделать так, чтобы одновременно только один поток мог работать с конкретным объектом ActiveMatch.
        match.addPoint(player.getName());
        saveFinishMatch(uuid);
        return matchMapper.mapToMatchInfoDto(match);
    }

    // TODO: Аннотация @Transactional здесь не работает, так как метод вызывается напрямую из другого метода этого же класса (self-invocation).
    @Transactional
    public void saveFinishMatch(UUID uuid) {
        ActiveMatch match = getActiveMatch(uuid);

        // TODO: Прямое обращение к полю match.winnerName нарушает инкапсуляцию — стоит создать и использовать match.isFinished().
        if (match.winnerName != null) {

            Player player1 = playerService.findPlayerByName(match.getPlayer1().getName());
            Player player2 = playerService.findPlayerByName(match.getPlayer2().getName());

            // Избыточный запрос для получения победителя — его можно вычислить из игроков.
            Player winner = playerService.findPlayerByName(match.winnerName);
            matchRepository.save(new Match(player1, player2, winner));
            matchSession.remove(uuid);
        }
    }

    // В Java методы должны начинаться с глагола. Лучше getFinishedMatches или findAllFinishedMatches.
    @Transactional(readOnly = true)
    public MatchesDTO allFinishMatches(String playerName, Pageable pageable) {
        Page<Match> matches;

        // Возможно так будет читаться лучше:
        /*
        Page<Match> matches = playerName == null ?
                matchRepository.findAll(pageable) :
                matchRepository.findByPlayerName(playerName, pageable);
        return matchMapper.mapToMatchesDto(matches);
         */
        if (playerName != null) {
            matches = matchRepository.findByPlayerName(playerName, pageable);
            return matchMapper.mapToMatchesDto(matches);
        }
        matches = matchRepository.findAll(pageable);
        return matchMapper.mapToMatchesDto(matches);
    }

    // Метод "получить счёт матча" возвращает "информацию о матче". Стоит переименовать DTO.
    public MatchInfoDTO getMatchScore(UUID uuid) {
        ActiveMatch match = getActiveMatch(uuid);
        return matchMapper.mapToMatchInfoDto(match);
    }

    private ActiveMatch getActiveMatch(UUID uuid) {
        ActiveMatch match = matchSession.get(uuid);
        if (match == null) {

            // Из сообщения в исключении не понятно, с каким именно ID не найден матч.
            throw new MatchNotFoundException("Match with this UUID not found");
        }
        return match;
    }
}
