package com.example.mathpracticeapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    private TextView questionTextView, scoreTextView;
    private Button optionA, optionB, optionC, optionD;

    private DifficultyLevel selectedLevel;
    private int currentQuestionIndex = 1;
    private int correctAnswers = 0;
    private int totalQuestions = 30;

    private int correctAnswer;
    private FirebaseUser currentUser;
    private String userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questionTextView = findViewById(R.id.questionTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);

        selectedLevel = DifficultyLevel.fromString(getIntent().getStringExtra("LEVEL"));

        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        loadNextQuestion();

        optionA.setOnClickListener(v -> checkAnswer(Integer.parseInt(optionA.getText().toString())));
        optionB.setOnClickListener(v -> checkAnswer(Integer.parseInt(optionB.getText().toString())));
        optionC.setOnClickListener(v -> checkAnswer(Integer.parseInt(optionC.getText().toString())));
        optionD.setOnClickListener(v -> checkAnswer(Integer.parseInt(optionD.getText().toString())));
    }

    private void loadNextQuestion() {
        QuestionData question = generateQuestion(selectedLevel);
        questionTextView.setText("Q" + currentQuestionIndex + ": " + question.questionText);
        correctAnswer = question.correctAnswer;

        ArrayList<Integer> options = new ArrayList<>();
        options.add(correctAnswer);
        Random random = new Random();
        while (options.size() < 4) {
            int wrong = correctAnswer + random.nextInt(20) - 10;
            if (wrong != correctAnswer && wrong >= 0 && !options.contains(wrong)) {
                options.add(wrong);
            }
        }

        Collections.shuffle(options);
        optionA.setText(String.valueOf(options.get(0)));
        optionB.setText(String.valueOf(options.get(1)));
        optionC.setText(String.valueOf(options.get(2)));
        optionD.setText(String.valueOf(options.get(3)));

        scoreTextView.setText("Score: " + correctAnswers + "/" + (currentQuestionIndex - 1));
    }

    private void checkAnswer(int selectedAnswer) {
        if (selectedAnswer == correctAnswer) {
            correctAnswers++;
        }

        currentQuestionIndex++;
        if (currentQuestionIndex > totalQuestions) {
            showSummaryDialog();
        } else {
            loadNextQuestion();
        }
    }

    private void showSummaryDialog() {
        double percentage = (correctAnswers * 100.0) / totalQuestions;
        Log.d(TAG, "UserName:"+userName+" userEmail"+userEmail);
        if (userEmail != null && userName != null) {
            Log.d(TAG, "Updating userStats");
            UserStatsManager.updateUserStats(userName, userEmail, selectedLevel.name(), correctAnswers, totalQuestions);
            Log.d(TAG, "Updating userStats successful");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Summary");
        builder.setMessage("Questions Attempted: " + totalQuestions +
                "\nCorrect Answers: " + correctAnswers +
                "\nPercentage: " + String.format("%.2f", percentage) + "%");
        builder.setPositiveButton("OK", (dialog, which) -> finish());
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            showSummaryDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private QuestionData generateQuestion(DifficultyLevel level) {
        Random rand = new Random();
        int a = 0, b = 0, answer = 0;
        String operator = "+";

        switch (level) {
            case EASY:
                a = rand.nextInt(9) + 1;
                b = rand.nextInt(9) + 1;
                operator = rand.nextBoolean() ? "+" : "-";
                break;
            case MEDIUM:
                a = rand.nextInt(91) + 10;
                b = rand.nextInt(91) + 10;
                operator = rand.nextBoolean() ? "+" : "-";
                break;
            case HARD:
                a = rand.nextInt(291) + 10;
                b = rand.nextInt(291) + 10;
                int op = rand.nextInt(3);
                operator = (op == 0) ? "+" : (op == 1) ? "-" : "*";
                break;
        }

        switch (operator) {
            case "+": answer = a + b; break;
            case "-": answer = a - b; break;
            case "*": answer = a * b; break;
        }

        return new QuestionData(a + " " + operator + " " + b + " = ?", answer);
    }

    private static class QuestionData {
        String questionText;
        int correctAnswer;

        QuestionData(String text, int answer) {
            this.questionText = text;
            this.correctAnswer = answer;
        }
    }
}
