package com.example.tennis.scoreboard.match;

public enum Points {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    AD("AD"); // Можно использовать полное имя ADVANTAGE

    private final String displayValue;

    Points(String displayValue) {
        this.displayValue = displayValue;
    }

    // Доменная модель не должна знать то, как она отображается во View — это нарушает Принцип единой ответственности (SRP).
        // В идеале эта логика должна быть в маппере.
    public String getDisplayValue() {
        return displayValue;
    }
}
