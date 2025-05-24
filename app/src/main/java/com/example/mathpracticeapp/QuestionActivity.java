package com.example.mathpracticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {

    private TextView questionText, scoreText, answerFeedbackText;
    private MaterialButton option1Button, option2Button, option3Button, option4Button;
    private MaterialButton exitButton, signOutButton;

    private String difficulty;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Question currentQuestion;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        difficulty = getIntent().getStringExtra("difficulty");

        // Link UI
        questionText = findViewById(R.id.questionText);
        scoreText = findViewById(R.id.scoreText);
        answerFeedbackText = findViewById(R.id.answerFeedbackText);

        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);

        exitButton = findViewById(R.id.exitButton);
        signOutButton = findViewById(R.id.signOutButton);

        option1Button.setOnClickListener(v -> checkAnswer(Integer.parseInt(option1Button.getText().toString())));
        option2Button.setOnClickListener(v -> checkAnswer(Integer.parseInt(option2Button.getText().toString())));
        option3Button.setOnClickListener(v -> checkAnswer(Integer.parseInt(option3Button.getText().toString())));
        option4Button.setOnClickListener(v -> checkAnswer(Integer.parseInt(option4Button.getText().toString())));

        exitButton.setOnClickListener(v -> finish());

        signOutButton.setOnClickListener(v -> signOut());

        loadNextQuestion();
    }

    private void checkAnswer(int userAnswer) {
        disableOptions();

        if (userAnswer == currentQuestion.getAnswer()) {
            score++;
            answerFeedbackText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            answerFeedbackText.setText("Correct!");
        } else {
            answerFeedbackText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            answerFeedbackText.setText(String.format(Locale.US, "Wrong! Correct: %d", currentQuestion.getAnswer()));
        }

        updateScore();

        handler.postDelayed(this::loadNextQuestion, 1500);
    }

    private void loadNextQuestion() {
        answerFeedbackText.setText(""); // Clear feedback
        enableOptions();

        currentQuestionIndex++;

        if (currentQuestionIndex > 30) {
            Toast.makeText(this, "Practice Complete!\nScore: " + score, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        currentQuestion = Question.generate(DifficultyLevel.valueOf(difficulty), currentQuestionIndex);
        questionText.setText("Q" + currentQuestionIndex + ": " + currentQuestion.getQuestionText());

        // Generate options with 1 correct + 3 random wrong answers
        List<Integer> options = generateOptions(currentQuestion.getAnswer());
        Collections.shuffle(options);

        option1Button.setText(String.valueOf(options.get(0)));
        option2Button.setText(String.valueOf(options.get(1)));
        option3Button.setText(String.valueOf(options.get(2)));
        option4Button.setText(String.valueOf(options.get(3)));
    }

    private List<Integer> generateOptions(int correctAnswer) {
        List<Integer> options = new ArrayList<>();
        options.add(correctAnswer);

        // Add 3 wrong answers close to correct answer
        int offset = 3;
        while (options.size() < 4) {
            int wrong = correctAnswer + (int) (Math.random() * offset * 2) - offset;
            if (wrong != correctAnswer && wrong >= 0 && !options.contains(wrong)) {
                options.add(wrong);
            }
        }
        return options;
    }

    private void updateScore() {
        scoreText.setText("Score: " + score);
    }

    private void disableOptions() {
        option1Button.setEnabled(false);
        option2Button.setEnabled(false);
        option3Button.setEnabled(false);
        option4Button.setEnabled(false);
    }

    private void enableOptions() {
        option1Button.setEnabled(true);
        option2Button.setEnabled(true);
        option3Button.setEnabled(true);
        option4Button.setEnabled(true);
    }

    private void signOut() {
        // Your sign-out logic here (e.g., FirebaseAuth.getInstance().signOut() or clearing stored user data)
        Toast.makeText(this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
        // Navigate back to login or main screen
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
