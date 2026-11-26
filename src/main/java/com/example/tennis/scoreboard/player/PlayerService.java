package com.example.tennis.scoreboard.player;

import com.example.tennis.scoreboard.player.dto.PlayersDTO;
import com.example.tennis.scoreboard.player.dto.PlayersDTORequest;
import com.example.tennis.scoreboard.exception.MessageDto;
import com.example.tennis.scoreboard.exception.PlayerAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Transactional
    // TODO: Метод создания игрока должен возвращать данные созданной сущности, а сообщение о создании.
    public MessageDto createPlayer(PlayersDTORequest request) {
        Player findPlayer = playerRepository.findByNameIgnoreCase(request.name());
        if (findPlayer != null) {

            // Ошибкой является не наличие игрока в БД, а нарушение уникальности имени.
            throw new PlayerAlreadyExistsException("Players already exist");
        }

        // Код с промежуточными переменными с понятными именами читается лучше,
            // чем вызовы методов, передаваемые в аргументы другим методам.
        playerRepository.save(new Player(request.name()));

        // TODO: Метод сервиса после успешного создания игрока не должен возвращать MessageDto из пакета exception.
        return new MessageDto("Player successfully created");
    }

    @Transactional(readOnly = true)
    // В названии больше подошёл бы глагол get или find вместо show.
    public List<PlayersDTO> showAllPlayers(Pageable pageable) {
        Page<Player> players = playerRepository.findAll(pageable);
        return playerMapper.mapToDto(players);
    }

    // Непоследовательное использование @Transactional:
        // над методом получения списка игроков есть @Transactional(readOnly = true),
        // а над методом получения одного игрока — нет.
    // Вслед за методом репозитория, этот метод может вернуть null. Лучше возвращать Optional<Player>.
        // А также в идеале из сервиса возвращать DTO, а не JPA Entity.
    public Player findPlayerByName(String name) {
        return playerRepository.findByNameIgnoreCase(name);
    }

}
