package com.example.tennis.scoreboard.match;

public class ActivePlayer {

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а значительная часть логики находится в другом классе.
        // Если бы у класса были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "reach-anemic-model.md" в этом же пакете)

    // TODO: Сейчас класс позволяет внешнему коду привести объект в некорректное состояние.
        // Например, установить одновременно счёт для тай-брейка и гейма, а также в любой момент времени сбросить очки.
        // Класс модели должен защищать свои инварианты и не позволять создание объекта в состоянии,
        // не соответствующем правилам модели.

    // TODO: Класс отвечает за хранение очков на всех этапах игрового процесса в матче — это слишком большая ответственность
        // для одного класса и нарушает SRP (Single Responsibility Principle).
        // Лучшим решением в этом направлении было бы, чтобы за счёт на каждом уровне (матч-сет-гейм) отвечал отдельный класс.
        // Такой подход больше соответствовал бы ООП-стилю и принципу единственной ответственности для каждого класса.

    // Поля games и sets не могут быть null — для них лучше использовать примитивный тип int вместо обёртки Integer.

    private String name;
    private Points points = Points.ZERO;
    private Integer games = 0;
    private Integer sets = 0;
    private Integer tieBreakPoints = null;

    // TODO: Конструктор без агрументов позволяет создать игрока без имени.
        // Класс модели должен защищать свои инварианты и не позволять создание объекта в состоянии,
        // не соответствующем правилам модели.
    public ActivePlayer() {
    }

    public ActivePlayer(String name) {
        this.name = name;
    }

    public ActivePlayer(String name, Points points, Integer games, Integer sets, Integer tieBreakPoints) {
        this.name = name;
        this.points = points;
        this.games = games;
        this.sets = sets;
        this.tieBreakPoints = tieBreakPoints;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Points getPoints() {
        return points;
    }

    // TODO: Метод нарушает Принцип наименьшего удивления
        // (см. файл "pola.md" в этом же пакете)
        // Названия с префиксом set принято использовать для установки значения, принимаемого в аргументы метода.
    public void setPoints() {
        this.points = switch (points) {
            case ZERO -> Points.FIFTEEN;
            case FIFTEEN -> Points.THIRTY;
            case THIRTY -> Points.FORTY;
            case FORTY -> Points.AD;

            // TODO: В логике установки следующего значения для points после AD нет значения.
                // Поэтому вместо "тихого" перехода AD -> Points.ZERO здесь лучше выбрасывать исключение,
                // а логику перехода к счёту в новом гейме обрабатывать в другом месте.
            case AD -> Points.ZERO;
        };
    }

    public void setPoints(Points points) {
        this.points = points;
    }

    public void resetPoints() {
        this.points = Points.ZERO;
    }

    public void winGame() {
        this.games += 1;
    }

    public Integer getGames() {
        return games;
    }

    public void incrementGames() {
        this.games ++;
    }

    public void resetGames() {
        this.games = 0;
    }

    public Integer getSets() {
        return sets;
    }

    public void incrementSets() {
        this.sets ++;
    }

    public void resetSets() {
        this.sets = 0;
    }

    public Integer getTieBreakPoints() {
        return tieBreakPoints;
    }

    public void setTieBreakPoints(Integer tieBreakPoints) {
        this.tieBreakPoints = tieBreakPoints;
    }

    public void incrementTieBreakPoints() {
        this.tieBreakPoints ++;
    }

    public void resetTieBreakPoints() {
        this.tieBreakPoints = 0;
    }

    @Override
    public String toString() {
        return "ActivePlayer{" +
                "name='" + name + '\'' +
                ", points=" + points +
                ", games=" + games +
                ", sets=" + sets +
                ", tieBreakPoints=" + tieBreakPoints +
                '}';
    }
}
