package com.example.mathpracticeapp;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class UserStatsManager {

    private static final String TAG = "UserStatsManager";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void updateUserStats(String name, String email, String level, int correctAnswers, int totalQuestions) {
        if (email == null || email.isEmpty()) {
            Log.e(TAG, "Email is null or empty. Cannot update stats.");
            return;
        }

        String levelLower = level.toLowerCase(Locale.ROOT);
        String fieldHighScore = "high_score_" + levelLower;
        String fieldAttempts = "attempts_" + levelLower;
        String fieldTotalScore = "total_score_" + levelLower;

        db.collection("user_stats").document(email)
                .get()
                .addOnSuccessListener(document -> {
                    int newAttempts = 1;
                    int newTotalScore = correctAnswers;
                    int highScore = correctAnswers;

                    if (document.exists()) {
                        Long prevAttempts = document.getLong(fieldAttempts);
                        Long prevTotalScore = document.getLong(fieldTotalScore);
                        Long prevHighScore = document.getLong(fieldHighScore);

                        newAttempts = (prevAttempts != null ? prevAttempts.intValue() : 0) + 1;
                        newTotalScore = (prevTotalScore != null ? prevTotalScore.intValue() : 0) + correctAnswers;
                        highScore = Math.max(correctAnswers, prevHighScore != null ? prevHighScore.intValue() : 0);
                    }

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("name", name);
                    updates.put("email", email);
                    updates.put(fieldHighScore, highScore);
                    updates.put(fieldAttempts, newAttempts);
                    updates.put(fieldTotalScore, newTotalScore);

                    db.collection("user_stats").document(email)
                            .set(updates, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Stats updated for " + email))
                            .addOnFailureListener(e -> Log.e(TAG, "Failed to update stats for " + email, e));
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to fetch existing stats", e));
    }

    public static void fetchLeaderboard(String level, Consumer<String> callback) {
        String levelLower = level.toLowerCase(Locale.ROOT);
        String fieldHighScore = "high_score_" + levelLower;

        db.collection("user_stats")
                .orderBy(fieldHighScore, com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    StringBuilder leaderboard = new StringBuilder();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String name = doc.getString("name");
                        Long score = doc.getLong(fieldHighScore);
                        leaderboard.append(name != null ? name : "Unknown")
                                .append(": ")
                                .append(score != null ? score : 0)
                                .append("\n");
                    }
                    callback.accept(leaderboard.toString());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to fetch leaderboard", e);
                    callback.accept("Failed to load leaderboard.");
                });
    }
}
