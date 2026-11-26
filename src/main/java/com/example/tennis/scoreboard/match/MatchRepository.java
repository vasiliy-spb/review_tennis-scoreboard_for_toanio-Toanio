package com.example.tennis.scoreboard.match;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // TODO: ID в Match имеет тип Integer, а не Long, поэтому стоит типизировать интерфейс так: JpaRepository<Match, Integer>.

    // Для выполнения регистронезависимого поиска по шаблону в PostgreSQL можно использовать оператор ILIKE.

    // TODO: Проблема N+1 в запросе выборки матчей.
        // Метод выборки списка матчей выполняет HQL-запрос вида `"SELECT m FROM Match m ..."`.
        // Сущность `Match` имеет связи `@ManyToOne` с `Player`, поэтому при выполнении такого запроса
        // Hibernate сначала получит список матчей (1 запрос), а затем он будет выполнять по 2 дополнительных `SELECT` запроса
        // для каждого матча, чтобы получить связанных с ним игроков. Если на странице 10 матчей,
        // это приведёт к 21 запросу (если все игроки будут разные) вместо одного.

    @Query("""
            SELECT m FROM Match m 
            WHERE LOWER(m.player1.name) = LOWER(:playerName )
            OR LOWER(m.player2.name) = LOWER(:playerName)
            """)
    // Точнее было бы назвать findAllByPlayerNameIgnoreCase
    Page<Match> findByPlayerName(@Param("playerName") String playerName, Pageable pageable);

}
