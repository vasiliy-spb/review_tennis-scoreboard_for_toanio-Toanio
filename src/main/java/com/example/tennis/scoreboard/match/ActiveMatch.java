package com.example.tennis.scoreboard.match;

import com.example.tennis.scoreboard.exception.PlayerNotFoundException;

public class ActiveMatch {

    // У некоторых полей отсутствует явный модификатор доступа — это ослабляет инкапсуляцию класса.

    // TODO: Класс отвечает за обработку счёта на всех этапах игрового процесса в матче — это слишком большая ответственность
        // для одного класса и нарушает SRP (Single Responsibility Principle).
        // Лучшим решением в этом направлении было бы, чтобы за логику на каждом уровне (матч-сет-гейм) отвечал отдельный класс.
        // Такой подход больше соответствовал бы ООП-стилю и принципу единственной ответственности для каждого класса.
        // А также это упростило бы чтение и поддержку кода.

    // TODO: Класс позволяет любому внешнему коду в произвольный момент изменять своё состояние (например через методы reset*).
        // Это является признаком анемичной модели.
        // (см. файл "reach-anemic-model.md" в этом же пакете)
        // Класс должен самостоятельно и полностью управлять своим состоянием,
        // предоставляя наружу только необходимые методы, для запуска этих изменений.

    // Все "магические числа" лучше вынести в именованные константы.

    // Составные условия из `if` требуют усилий для понимания. Сложные логические выражения ухудшают читаемость кода
        // и увеличивают вероятность ошибки при их написании или изменении.
        // Лучше вводить поясняющие переменные или выносить такие условия
        // в отдельный `private`-метод с понятным названием, которое описывает бизнес-правило.

    // TODO: Риск NPE: в методах startTieBreak и stopTieBreak зануляются обычные очки, когда начинается тай-брейк, и наоборот.
        // При этом нигде в коде нет проверок на null (например, в checkDeuce или isWinningPoint).

    private final ActivePlayer player1;
    private final ActivePlayer player2;

    // TODO: В MatchService есть прямое обращение к этому полю.
        // Прямой доступ к полям класса нарушает инкапсуляцию и создаёт сильную связанность между классами:
        // любой класс из того же пакета может не только прочитать это поле, но и случайно перезаписать его, сломав состояние матча.
        // Поле должно быть private, а для безопасного чтения стоит предоставить геттер (или метод isFinished()).
    String winnerName = null;
    boolean isTieBreak = false;

    // Константы объявляются первыми (пишутся в самом верху) в классе.
    // У константы должен быть модификатор static.
    final int SET_TO_WIN = 2;

    // TODO: Модель матча должна создавать объекты счёты для игроков, а не принимать их в конструктор.
        // Объекты ActivePlayer не имеют смысла сами по себе в отрыве от ActiveMatch,
        // поэтому не должны создавать где-то вовне.
    public ActiveMatch(ActivePlayer player1, ActivePlayer player2) {
        this.player1 = player1;
        this.player2 = player2;

        if (player1.getGames() == 6 && player2.getGames() == 6) {
            startTieBreak(player1, player2);
        }
    }

    public void addPoint(String playerName) {

        // TODO: Нет проверки на то, что матч не завершён.
            // Попытка начислить очко в уже завершённом матче — это не нормальная ситуация и должна приводить к исключению.

        ActivePlayer player = findPlayer(playerName);
        ActivePlayer opponent = getOpponent(player);

        if (isTieBreak) {
            player.incrementTieBreakPoints();
            checkTieBreakPoints(player, opponent);
        } else {
            scoreRegularGame(player, opponent);
        }
    }

    private void scoreRegularGame(ActivePlayer player, ActivePlayer opponent) {
        if (checkDeuce(player, opponent)) {
            player.setPoints(Points.AD);
        } else if (isOpponentAD(opponent)) {
            player.setPoints(Points.FORTY);
            opponent.setPoints(Points.FORTY);
        } else if (isWinningPoint(player, opponent) || isPlayerAD(player)) {
            player.incrementGames();
            resetPoints(player, opponent);
            checkSets(player, opponent);
        } else {
            player.setPoints();
        }
    }

    private void checkSets(ActivePlayer player, ActivePlayer opponent) {
        if (player.getGames() >= 6 && player.getGames() - opponent.getGames() >= 2) {
            player.incrementSets();
            checkMatchWinner(player);
            resetGames(player, opponent);
        } else if (player.getGames() == 6 && opponent.getGames() == 6) {
            startTieBreak(player, opponent);
        }
    }

    private void checkTieBreakPoints(ActivePlayer player, ActivePlayer opponent) {
        if (player.getTieBreakPoints() >= 7 && player.getTieBreakPoints() - opponent.getTieBreakPoints() >= 2) {
            stopTieBreak(player, opponent);
            player.incrementSets();
            checkMatchWinner(player);
            resetGames(player, opponent);
        }
    }

    private void checkMatchWinner(ActivePlayer player) {
        if (player.getSets() == SET_TO_WIN) {
            winnerName = player.getName();
        }
    }

    private boolean checkDeuce(ActivePlayer player, ActivePlayer opponent) {
        return opponent.getPoints() == Points.FORTY && player.getPoints() == Points.FORTY;
    }

    // Вместо двух методов isOpponentAD и isPlayerAD достаточно было бы иметь один hasAdvantage(ActivePlayer player).
    private boolean isOpponentAD(ActivePlayer opponent) {
        return opponent.getPoints() == Points.AD;
    }

    private boolean isPlayerAD(ActivePlayer player) {
        return player.getPoints() == Points.AD;
    }

    private boolean isWinningPoint(ActivePlayer player, ActivePlayer opponent) {
        return player.getPoints() == Points.FORTY
                && opponent.getPoints() != Points.FORTY
                && opponent.getPoints() != Points.AD;
    }

    // Метод не выполняет поиск, потому лучше назвать его get*
    private ActivePlayer findPlayer(String playerName) {
        if (playerName.equals(player1.getName())) {
            return player1;
        }

        if (playerName.equals(player2.getName())) {
            return player2;
        }

        // TODO: Исключение "игрок не найден" не подходит для метода, который не выполняет поиск.
            // Неправильные исключения вводят в заблуждение и усложняют отладку кода.
        throw new PlayerNotFoundException("Player " + playerName + " not found");
    }

    private ActivePlayer getOpponent(ActivePlayer player) {
        if (player == player1) {
            return player2;
        }
        return player1;
    }

    public void startTieBreak(ActivePlayer player, ActivePlayer opponent) {
        isTieBreak = true;
        player.setPoints(null);
        opponent.setPoints(null);

        if (player.getTieBreakPoints() == null) {
            player.setTieBreakPoints(0);
        }

        if (opponent.getTieBreakPoints() == null) {
            opponent.setTieBreakPoints(0);
        }
    }

    public void stopTieBreak(ActivePlayer player, ActivePlayer opponent) {
        isTieBreak = false;

        player.setTieBreakPoints(null);
        player.setPoints(Points.ZERO);

        opponent.setTieBreakPoints(null);
        opponent.setPoints(Points.ZERO);
    }

    public void resetPoints(ActivePlayer player, ActivePlayer opponent) {
        player.resetPoints();
        opponent.resetPoints();
    }

    public void resetGames(ActivePlayer player, ActivePlayer opponent) {
        player.resetGames();
        opponent.resetGames();
    }

    public void resetTieBreakPoints(ActivePlayer player, ActivePlayer opponent) {
        player.resetTieBreakPoints();
        opponent.resetTieBreakPoints();
    }

    public ActivePlayer getPlayer1() {
        return player1;
    }

    public ActivePlayer getPlayer2() {
        return player2;
    }
}
