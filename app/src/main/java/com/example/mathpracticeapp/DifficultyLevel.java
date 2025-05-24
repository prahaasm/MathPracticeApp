package com.example.mathpracticeapp;

public enum DifficultyLevel {
    EASY, MEDIUM, HARD;

    public static DifficultyLevel fromString(String level) {
        if (level == null) throw new IllegalArgumentException("Difficulty level cannot be null");
        switch (level.toLowerCase()) {
            case "easy": return EASY;
            case "medium": return MEDIUM;
            case "hard": return HARD;
            default: throw new IllegalArgumentException("Unknown difficulty: " + level);
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case EASY: return "Easy";
            case MEDIUM: return "Medium";
            case HARD: return "Hard";
            default: return super.toString();
        }
    }
}
