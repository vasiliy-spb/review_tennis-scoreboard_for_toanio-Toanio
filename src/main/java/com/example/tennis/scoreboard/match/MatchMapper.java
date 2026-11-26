package com.example.tennis.scoreboard.match;

import com.example.tennis.scoreboard.match.dto.FinishedMatchDTO;
import com.example.tennis.scoreboard.match.dto.MatchInfoDTO;
import com.example.tennis.scoreboard.match.dto.MatchesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = ActivePlayerMapper.class) // Для "spring" в mapstruct есть специальная константа: MappingConstants.ComponentModel.SPRING
public interface MatchMapper {

    // Можно разделить на FinishedMatchMapper и ActiveMatchMapper.

    @Mapping(source = "player1", target = "firstPlayer")
    @Mapping(source = "player2", target = "secondPlayer")

    // Можно просто mapToDto
    MatchInfoDTO mapToMatchInfoDto(ActiveMatch activeMatch);

    @Mapping(source = "player1.name", target = "firstPlayerName")
    @Mapping(source = "player2.name", target = "secondPlayerName")
    @Mapping(source = "winner.name", target = "winnerName")

    // Можно просто mapToDto
    FinishedMatchDTO mapToFinishMatchDto(Match match);

    // Можно mapToListDto
    List<FinishedMatchDTO> mapToFinishList (Page<Match> matches);

    // Можно mapToPageDto
    default MatchesDTO mapToMatchesDto(Page<Match> matches) {
        List<FinishedMatchDTO> finishedMatches = mapToFinishList(matches);

        return new MatchesDTO(
                finishedMatches,
                matches.getNumber(),
                matches.getTotalPages());
    }

    // Стоит удалять закомментированный код перед тем, как выполнять коммит
    //List<MatchesDTO> mapToMatchesDto(List<FinishedMatchDTO> matches);
}
