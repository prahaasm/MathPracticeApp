package com.example.mathpracticeapp;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnPractice;
    private SignInButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPractice = findViewById(R.id.btnPractice);
        btnSignIn = findViewById(R.id.btnSignIn);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check if user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            btnPractice.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
        }

        btnSignIn.setOnClickListener(v -> signIn());

        btnPractice.setOnClickListener(v -> {
            GoogleSignInAccount signedInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (signedInAccount != null) {
                Intent intent = new Intent(MainActivity.this, PracticeActivity.class);
                intent.putExtra("userName", signedInAccount.getDisplayName()); // pass username
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please sign in first", Toast.LENGTH_SHORT).show();
            }
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

                // Signed in successfully
                Toast.makeText(this, "Signed in as: " + account.getEmail(), Toast.LENGTH_SHORT).show();

                // Show Practice Mode button and hide Sign-In button
                btnPractice.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.GONE);

            } catch (ApiException e) {
                Toast.makeText(this, "Sign in failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
