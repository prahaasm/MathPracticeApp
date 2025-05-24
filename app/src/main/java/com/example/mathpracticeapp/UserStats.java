package com.example.mathpracticeapp;

public class UserStats {
    public String name;
    public String email;
    public int highScoreEasy;
    public int highScoreMedium;
    public int highScoreHard;
    public int attemptsEasy;
    public int attemptsMedium;
    public int attemptsHard;
    public float averageScoreEasy;
    public float averageScoreMedium;
    public float averageScoreHard;

    public UserStats() {} // Needed for Firestore

    public UserStats(String name, String email) {
        this.name = name;
        this.email = email;
        this.highScoreEasy = 0;
        this.highScoreMedium = 0;
        this.highScoreHard = 0;
        this.attemptsEasy = 0;
        this.attemptsMedium = 0;
        this.attemptsHard = 0;
        this.averageScoreEasy = 0f;
        this.averageScoreMedium = 0f;
        this.averageScoreHard = 0f;
    }
}
