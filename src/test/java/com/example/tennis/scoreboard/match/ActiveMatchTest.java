package com.example.tennis.scoreboard.match;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ActiveMatchTest {

    // Есть полезная аннотация org.junit.jupiter.api.BeforeEach.
        // Метод с ней поможет избавиться от множественного дублирования в классе.

    // После рефакторинга слоя моделей стоит продумать для каждого нового класса детальные тесты,
        // покрывающие основные, крайние и негативные кейсы.

    // Стоит давать тестам говорящие названия, из которых понятно, какое поведение и при каких условиях проверяется.
        // Например, shouldIncrementPointsWhenPlayerScores вместо checkWinPoints, или использовать @DisplayName.
        // Особенно это касается имён вроде checkAD и checkAD2.

    // Также для лучшей читаемости стоит разделять тело теста пустыми строками на три логических блока (Arrange — Act — Assert)
        // и использовать static import для методов Assertions.

    // Полю не хватает модификатора private.
    ActiveMatch match;

    // Маппер не нужен в тестах класса модели.
    ActivePlayerMapper mapper;

    @Test
    public void checkWinPoints() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.ZERO, 6, 0, 0);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FIFTEEN, 0, 0, 0);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");
        match.addPoint("Anton");

        Assertions.assertEquals(Points.FIFTEEN, player1.getPoints());
        Assertions.assertEquals(Points.THIRTY, player2.getPoints());
    }

    @Test
    public void checkWinGames() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 0, 0, 0);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FIFTEEN, 0, 0, 0);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertEquals(1, player1.getGames());
    }

    @Test
    public void checkAD() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 0, 0, 0);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FORTY, 0, 0, 0);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertEquals(Points.AD, player1.getPoints());
    }

    // Неинформативное название теста с цифрой в конце.
    @Test
    public void checkAD2() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 0, 0, 0);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.AD, 0, 0, 0);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertEquals(Points.FORTY, player1.getPoints());
        Assertions.assertEquals(Points.FORTY, player2.getPoints());
    }

    @Test
    public void checkWinGamesAfterAD() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.AD, 0, 0, 0);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FORTY, 0, 0, 0);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertEquals(Points.ZERO, player1.getPoints());
        Assertions.assertEquals(1, player1.getGames());
    }

    @Test
    public void checkWinSets() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 6, 0, 0);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FIFTEEN, 2, 0, 0);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertEquals(Points.ZERO, player1.getPoints());
        Assertions.assertEquals(0, player1.getGames());
        Assertions.assertEquals(1, player1.getSets());
    }

    @Test
    public void checkTieBreakPoints() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 6, 0, 0);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FIFTEEN, 6, 0, 0);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertEquals(1, player1.getTieBreakPoints());
    }

    @Test
    public void checkPointsNull() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 6, 0, 1);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FIFTEEN, 6, 0, 3);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertNull(player1.getPoints());
        Assertions.assertNull(player2.getPoints());
    }

    @Test
    public void checkLongTieBreakPoints() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 6, 0, 6);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FIFTEEN, 6, 0, 6);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");
        Assertions.assertEquals(0, player1.getSets());
        Assertions.assertTrue(match.isTieBreak);

        match.addPoint("Oleg");
        Assertions.assertEquals(1, player1.getSets());
        Assertions.assertFalse(match.isTieBreak);
    }

    @Test
    public void checkWinSetsByTieBreakPoints() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 6, 0, 6);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FIFTEEN, 6, 0, 3);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertEquals(1, player1.getSets());
    }

    @Test
    public void checkMatchWinnerRegularGame() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 5, 1, 0);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FIFTEEN, 4, 0, 0);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertEquals("Oleg", match.winnerName);
    }

    @Test
    public void checkMatchWinnerByTieBreakPoints() {
        ActivePlayer player1 = new ActivePlayer("Oleg", Points.FORTY, 6, 1, 6);
        ActivePlayer player2 = new ActivePlayer("Anton", Points.FIFTEEN, 6, 0, 3);
        match = new ActiveMatch(player1, player2);
        match.addPoint("Oleg");

        Assertions.assertEquals("Oleg", match.winnerName);
    }

}
