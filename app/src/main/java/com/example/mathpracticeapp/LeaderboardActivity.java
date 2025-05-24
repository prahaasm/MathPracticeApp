package com.example.mathpracticeapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LeaderboardActivity extends AppCompatActivity {

    private TextView easyLeaderboard, mediumLeaderboard, hardLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        easyLeaderboard = findViewById(R.id.easyLeaderboard);
        mediumLeaderboard = findViewById(R.id.mediumLeaderboard);
        hardLeaderboard = findViewById(R.id.hardLeaderboard);

        // Load all three leaderboards
        UserStatsManager.fetchLeaderboard("EASY", leaderboardText ->
                easyLeaderboard.setText("Easy Leaderboard\n\n" + leaderboardText)
        );

        UserStatsManager.fetchLeaderboard("MEDIUM", leaderboardText ->
                mediumLeaderboard.setText("Medium Leaderboard\n\n" + leaderboardText)
        );

        UserStatsManager.fetchLeaderboard("HARD", leaderboardText ->
                hardLeaderboard.setText("Hard Leaderboard\n\n" + leaderboardText)
        );
    }
}
