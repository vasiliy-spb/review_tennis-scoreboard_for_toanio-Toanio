package com.example.tennis.scoreboard.player;

import com.example.tennis.scoreboard.player.dto.PlayersDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring") // Для "spring" в mapstruct есть специальная константа: MappingConstants.ComponentModel.SPRING
public interface PlayerMapper {
    PlayersDTO mapToDto(Player player);

    // Можно назвать mapToListDto
    List<PlayersDTO> mapToDto(Page<Player> players);
}
