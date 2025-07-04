package com.example.mathpracticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PracticeActivity extends AppCompatActivity {

    private Button easyBtn, mediumBtn, hardBtn;
    private String userName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_practice);

        easyBtn = findViewById(R.id.easyBtn);
        mediumBtn = findViewById(R.id.mediumBtn);
        hardBtn = findViewById(R.id.hardBtn);

        // Get username from intent extras
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        easyBtn.setOnClickListener(v -> startQuestionActivity("Easy"));
        mediumBtn.setOnClickListener(v -> startQuestionActivity("Medium"));
        hardBtn.setOnClickListener(v -> startQuestionActivity("Hard"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();  // Close this activity and return to MainActivity
        return true;
    }


    private void startQuestionActivity(String level) {
        Intent intent = new Intent(PracticeActivity.this, QuestionActivity.class);
        intent.putExtra("LEVEL", level);
        intent.putExtra("userName", userName);  // pass username forward
        startActivity(intent);
    }
}
