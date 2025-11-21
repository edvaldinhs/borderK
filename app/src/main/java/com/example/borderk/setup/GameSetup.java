package com.example.borderk.setup;

public class GameSetup {
    private String gameId;
    private String title;
    private int numberOfSteps;

    public GameSetup(String gameId, String title, int numberOfSteps) {
        this.gameId = gameId;
        this.title = title;
        this.numberOfSteps = numberOfSteps;
    }

    public String getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }
}