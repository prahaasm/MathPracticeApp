package com.example.mathpracticeapp;

import java.util.Random;

public class Question {
    private static final Random random = new Random();

    private final String questionText;
    private final int answer;

    public Question(String text, int answer) {
        this.questionText = text;
        this.answer = answer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getAnswer() {
        return answer;
    }

    public static Question generate(DifficultyLevel difficulty, int questionIndex) {
        switch (difficulty) {
            case EASY:
                if (questionIndex <= 10) return add(1, 9);
                else if (questionIndex <= 20) return subtract(1, 9);
                else return random.nextBoolean() ? add(1, 9) : subtract(1, 9);

            case MEDIUM:
                if (questionIndex <= 10) return add(10, 100);
                else if (questionIndex <= 20) return subtract(10, 100);
                else return random.nextBoolean() ? add(10, 100) : subtract(10, 100);

            case HARD:
                if (questionIndex <= 10) return add(10, 300);
                else if (questionIndex <= 20) return subtract(10, 300);
                else {
                    int choice = random.nextInt(3);
                    if (choice == 0) return add(10, 300);
                    else if (choice == 1) return subtract(10, 300);
                    else return multiply(10, 99, 2, 9);
                }

            default:
                throw new IllegalArgumentException("Unsupported difficulty");
        }
    }

    private static Question add(int min, int max) {
        int a = rand(min, max), b = rand(min, max);
        return new Question(a + " + " + b, a + b);
    }

    private static Question subtract(int min, int max) {
        int a = rand(min, max), b = rand(min, max);
        int big = Math.max(a, b), small = Math.min(a, b);
        return new Question(big + " - " + small, big - small);
    }

    private static Question multiply(int aMin, int aMax, int bMin, int bMax) {
        int a = rand(aMin, aMax), b = rand(bMin, bMax);
        return new Question(a + " × " + b, a * b);
    }

    private static int rand(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
