package com.example.mathpracticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PracticeActivity extends AppCompatActivity {

    private Button easyBtn, mediumBtn, hardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        easyBtn = findViewById(R.id.easyBtn);
        mediumBtn = findViewById(R.id.mediumBtn);
        hardBtn = findViewById(R.id.hardBtn);

        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestionActivity("Easy");
            }
        });

        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestionActivity("Medium");
            }
        });

        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestionActivity("Hard");
            }
        });
    }

    private void startQuestionActivity(String level) {
        Intent intent = new Intent(PracticeActivity.this, QuestionActivity.class);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
    }
}
