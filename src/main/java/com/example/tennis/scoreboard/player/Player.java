package com.example.tennis.scoreboard.player;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    // TODO: Нет индекса по имени для БД (должен быть по ТЗ).
        // Его можно задать или через @Column(unique = true) или через аннотацию над классом
        // Можно задать индекс через аннотацию, чтобы у него было понятное имя — @Table(name = "players", indexes = @Index(...))

    // У полей отсутствует явный модификатор доступа — это ослабляет инкапсуляцию класса.

    // TODO: сеттеры не нужны — позволяют создать объект с установленным id или изменить имя игрока после создания.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name") // Стоит добавить nullable = false и length = 30
    String name;

    // Спецификация JPA требует наличия конструктора без аргументов для создания экземпляров сущностей,
        // однако ему не обязательно быть `public`. Когда конструктор публичный, он становится частью общедоступного API класса.
        // Это позволяет использовать его для создания "пустых", невалидных объектов (без установки обязательных полей)
        // в любом месте приложения, хотя он предназначен исключительно для внутреннего использования фреймворком (JPA).
        //
        // Хорошим подходом будет ограничить область видимости этого конструктора до `protected`.
        // Это делает его недоступным для прямого вызова из других пакетов, но оставляет видимым для JPA и дочерних классов.
    public Player() {
    }

    // TODO: ID в этом проекте генерируется и устанавливается в Entity фреймворком,
        // а конструктор с ID позволяет создать объект с установленным id из приложения.
    public Player(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    // TODO: сеттер для имени не нужен — имя игрока уникально и неизменно в этом проекте,
        // а сеттер позволяет изменить имя игрока после создания.
    public void setName(String name) {
        this.name = name;
    }
}
