package com.example.tennis.scoreboard.player;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

     // TODO: ID в Player имеет тип Integer, а не Long, поэтому стоит типизировать интерфейс так: JpaRepository<Player, Integer>.

     // Лучше возвращать Optional<Player>, чтобы метод никогда не возвращал null.
     Player findByNameIgnoreCase(String name);
}
