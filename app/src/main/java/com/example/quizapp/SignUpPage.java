package com.example.quizapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPage extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText name;

    Button signup;
    ProgressBar progressBar;
    private Dialog progressDialog;
    private TextView dialogText;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgin_up_page);
        email = findViewById(R.id.editTextSignUPEmailAddress);
        password = findViewById(R.id.editTextSignUpPassword);
        name = findViewById(R.id.editTextTextPersonName);
        signup = findViewById(R.id.buttonSignUp);

        progressDialog = new Dialog(SignUpPage.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("registration process");

//        progressBar = findViewById(R.id.progressBarSignUp);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup.setClickable(false);
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if (vaildateDate()) {
                    signUpFireBase(userEmail, userPassword);
                }

            }
        });
    }

    private boolean vaildateDate() {
        boolean status = false;
        if (email.getText().toString().isEmpty()) {
            email.setError("Enter your E-mail please");
            return false;
        }
        if (email.length() < 4) {
            email.setError("Enter a Vaild E-mail");
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Enter your a password");
            return false;
        }
        if (password.length() < 8) {
            password.setError("Enter a password  of 8 characters");
            return false;
        }
        return true;
    }

    public void signUpFireBase(String userEmail, String userPassword) {

        progressDialog.show();
        String userName = name.getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpPage.this, "Account has been created", Toast.LENGTH_LONG).show();
                            DataBase.createUser(userEmail, userName, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    DataBase.loadUserDate(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();
                                            Intent i = new Intent(SignUpPage.this, MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUpPage.this, "Error Fetching Data", Toast.LENGTH_LONG).show();

                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpPage.this, "Error, try again later", Toast.LENGTH_LONG).show();

                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpPage.this, "Error please try again later", Toast.LENGTH_LONG).show();


                        }

                    }

                });

    }
}