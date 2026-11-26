package com.example.tennis.scoreboard.match;

import com.example.tennis.scoreboard.player.Player;
import jakarta.persistence.*;

@Entity
@Table(name = "matches") // "matches" является зарезервированным словом в некоторых СУБД.
    // Здесь проблем не будет, но лучше не выбирать такие названия. (см. файл "sql-keywords.md" в этом же пакете)
public class Match {

    // У полей отсутствует явный модификатор доступа — это ослабляет инкапсуляцию класса.

    // Стоит добавить проверки, что игроки разные и победитель один из игроков. Например, через аннотацию org.hibernate.annotations.Check над классом.

    // Связи `@ManyToOne` не имеют явного указания о стратегии загрузки.
        // По умолчанию для `@ManyToOne` используется `FetchType.EAGER`, что приводит к немедленной загрузке связанных сущностей при загрузке `Match`.
        // Это может вызывать проблемы производительности (N+1 запросов) и излишнюю загрузку данных, особенно если связанные объекты не всегда нужны.

    // TODO: сеттеры не нужны — позволяют создать объект с установленным id или изменить состав игроков после создания
        // (например сделать постороннего игрока победителем).

    // Для обязательных полей стоит добавить `optional = false` в `@ManyToOne`
        // или `nullable = false` в `@JoinColumn` (можно добавить оба параметра).
        // Целостность данных должна обеспечиваться на всех уровнях: в приложении (валидация)
        // и в БД (constraints). Отсутствие ограничений в БД означает,
        // что данные могут быть испорчены из-за ошибок в приложении или при прямом доступе к БД.

    // Колонки игроков и победителя в `@JoinColumn` названы `player1`, `player2`, `winner`.
        // Для колонок, хранящих внешний ключ, уместно добавлять суффикс `_id`, чтобы было очевидно, что в них хранится идентификатор, а не какая-то другая информация.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "player1")
    Player player1;

    @ManyToOne
    @JoinColumn(name = "player2")
    Player player2;

    @ManyToOne
    @JoinColumn(name = "winner")
    Player winner;

    // Спецификация JPA требует наличия конструктора без аргументов для создания экземпляров сущностей,
        // однако ему не обязательно быть `public`. Когда конструктор публичный, он становится частью общедоступного API класса.
        // Это позволяет использовать его для создания "пустых", невалидных объектов (без установки обязательных полей)
        // в любом месте приложения, хотя он предназначен исключительно для внутреннего использования фреймворком (JPA).
        //
        // Хорошим подходом будет ограничить область видимости этого конструктора до `protected`.
        // Это делает его недоступным для прямого вызова из других пакетов, но оставляет видимым для JPA и дочерних классов.
    public Match() {
    }

    // TODO: ID в этом проекте генерируется и устанавливается в Entity фреймворком,
        // а конструктор с ID позволяет создать объект с установленным id из приложения.
    public Match(Integer id, Player player1, Player player2, Player winner) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

    public Match(Player player1, Player player2, Player winner) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
