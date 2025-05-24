package com.example.mathpracticeapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnPractice, btnLeaderboard;
    private SignInButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        btnPractice = findViewById(R.id.btnPractice);
        btnLeaderboard = findViewById(R.id.btnLeaderboard);
        btnSignIn = findViewById(R.id.btnSignIn);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check if user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Show buttons
            btnPractice.setVisibility(View.VISIBLE);
            btnLeaderboard.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
        }

        btnSignIn.setOnClickListener(v -> signIn());

        btnPractice.setOnClickListener(v -> {
            GoogleSignInAccount signedInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (signedInAccount != null) {
                // Pass username/email if needed
                Intent intent = new Intent(MainActivity.this, PracticeActivity.class);
                intent.putExtra("userName", signedInAccount.getDisplayName());
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please sign in first", Toast.LENGTH_SHORT).show();
            }
        });

        btnLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Toast.makeText(this, "Signed in as: " + account.getEmail(), Toast.LENGTH_SHORT).show();

                btnPractice.setVisibility(View.VISIBLE);
                btnLeaderboard.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.GONE);

                // Start PracticeActivity and pass user info
                Intent intent = new Intent(MainActivity.this, PracticeActivity.class);
                intent.putExtra("userName", account.getDisplayName());
                intent.putExtra("userEmail", account.getEmail());
                startActivity(intent);
                finish();  // Optional: finish MainActivity so user can't come back by back button

            } catch (ApiException e) {
                Toast.makeText(this, "Sign in failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
