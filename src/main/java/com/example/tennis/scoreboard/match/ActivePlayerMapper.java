package com.example.tennis.scoreboard.match;

import com.example.tennis.scoreboard.match.dto.ActivePlayerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Для "spring" в mapstruct есть специальная константа: MappingConstants.ComponentModel.SPRING
public interface ActivePlayerMapper {

    // Раз для преобразования из модели в DTO создан отдельный компонент — маппер, было бы более уместным,
        // чтобы именно он знал о том, как отображается счёт в гейме, а не класс модели счёта.

    @Mapping(target = "points", expression = "java(player.getPoints().getDisplayValue())")
    ActivePlayerDTO mapToDto(ActivePlayer player);
}
