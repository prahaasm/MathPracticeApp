package com.example.mathpracticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    private TextView userNameText, questionText, resultPrompt;
    private Button option1, option2, option3, option4, signOutButton;

    private String userName;
    private DifficultyLevel difficultyLevel;
    private Question currentQuestion;

    private Random random = new Random();

    private int score = 0;
    private int questionCount = 0;
    private static final int MAX_QUESTIONS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        userNameText = findViewById(R.id.userNameText);
        questionText = findViewById(R.id.questionText);
        resultPrompt = findViewById(R.id.resultPrompt);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        signOutButton = findViewById(R.id.signOutButton);

        // Get username and difficulty from intent
        userName = getIntent().getStringExtra("userName");
        String levelString = getIntent().getStringExtra("LEVEL");

        try {
            difficultyLevel = DifficultyLevel.fromString(levelString);
        } catch (IllegalArgumentException e) {
            difficultyLevel = DifficultyLevel.EASY; // fallback
        }

        userNameText.setText("Hello, " + (userName != null ? userName : "Player"));

        loadNewQuestion();

        View.OnClickListener optionClickListener = v -> {
            disableOptions();

            Button clickedButton = (Button) v;
            int selectedAnswer;
            try {
                selectedAnswer = Integer.parseInt(clickedButton.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid answer option", Toast.LENGTH_SHORT).show();
                enableOptions();
                return;
            }

            if (selectedAnswer == currentQuestion.getAnswer()) {
                score++;
                resultPrompt.setText("Correct! Score: " + score);
                resultPrompt.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                resultPrompt.setText("Incorrect! Correct answer: " + currentQuestion.getAnswer() + "\nScore: " + score);
                resultPrompt.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            clickedButton.postDelayed(() -> {
                enableOptions();
                loadNewQuestion();
            }, 1500);
        };

        option1.setOnClickListener(optionClickListener);
        option2.setOnClickListener(optionClickListener);
        option3.setOnClickListener(optionClickListener);
        option4.setOnClickListener(optionClickListener);

        signOutButton.setOnClickListener(v -> {
            GoogleSignInHelper.signOut(this, () -> {
                Intent intent = new Intent(QuestionActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        });
    }

    private void loadNewQuestion() {
        if (questionCount >= MAX_QUESTIONS) {
            Toast.makeText(this, "Quiz Finished! Final Score: " + score, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        questionCount++;
        resultPrompt.setText("");
        currentQuestion = Question.generate(difficultyLevel, random.nextInt(30) + 1);
        questionText.setText(currentQuestion.getQuestionText());

        List<Integer> options = new ArrayList<>();
        options.add(currentQuestion.getAnswer());

        while (options.size() < 4) {
            int wrongAnswer = generateWrongAnswer(currentQuestion.getAnswer());
            if (!options.contains(wrongAnswer)) {
                options.add(wrongAnswer);
            }
        }

        Collections.shuffle(options);

        option1.setText(String.valueOf(options.get(0)));
        option2.setText(String.valueOf(options.get(1)));
        option3.setText(String.valueOf(options.get(2)));
        option4.setText(String.valueOf(options.get(3)));
    }

    private int generateWrongAnswer(int correctAnswer) {
        int offset = random.nextInt(10) + 1;
        return random.nextBoolean() ? correctAnswer + offset : Math.max(0, correctAnswer - offset);
    }

    private void disableOptions() {
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
    }

    private void enableOptions() {
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
    }
}
