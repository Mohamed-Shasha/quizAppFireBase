package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    Button resetPassword;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initialize fields
        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.editTextForgotPassword);
        resetPassword = findViewById(R.id.buttonResetPassword);
        progressBar = findViewById(R.id.progressBarForgotPassword);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = email.getText().toString().trim();
                setResetPassword(userEmail);
            }
        });
    }

    public void setResetPassword(String userEmail) {
//      send reset email to email inserted
        firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "a password reset email has been sent", Toast.LENGTH_SHORT).show();
                    resetPassword.setClickable(false);
                    progressBar.setVisibility(View.VISIBLE);
                    Intent i = new Intent(ForgotPassword.this, LoginPage.class);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(ForgotPassword.this, "Error please try again later!", Toast.LENGTH_SHORT).show();
                    resetPassword.setClickable(true);
                }

            }
        });


    }
}