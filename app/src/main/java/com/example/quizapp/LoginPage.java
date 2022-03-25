package com.example.quizapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private EditText email;
    private EditText password;
    private Button signIn;
    private SignInButton signInGoogle;
    private TextView signUp;
    private TextView forgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.editTextLoginEmailAddress);
        password = findViewById(R.id.editTextLoginPassword);
        signIn = findViewById(R.id.buttonLogin);
        signInGoogle = findViewById(R.id.buttonLoginGoogle);
        signUp = findViewById(R.id.textViewSignUp);
        forgotPassword = findViewById(R.id.textViewLoginForgotPassword);
//        progressBar = findViewById(R.id.progressBarLogin);

        progressDialog = new Dialog(LoginPage.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("Log in process");


        mAuth = FirebaseAuth.getInstance();
//        registerActivityForGoogleSignIn();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("647543851552-61phea01hc7cnb5j51bj3mb31i0pbeuo.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                if (vaildateDate()) {

                    signInWithFirebase(userEmail, userPassword);

                }
            }
        });
        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(i);

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginPage.this, ForgotPassword.class);
                startActivity(i);
                finish();
            }
        });


    }

    private boolean vaildateDate() {
        boolean status = false;
        if (email.getText().toString().isEmpty()) {
            email.setError("Enter your E-mail please");
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Enter your E-mail please");
            return false;
        }
        return true;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginPage.this, e.toString(), Toast.LENGTH_SHORT).show();

            }
        }
    }


    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginPage.this, "Google Signing in Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
//                                new user created in database
                                assert user != null;
                                DataBase.createUser(user.getEmail().toString(), user.getDisplayName().toString(), new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {

                                        DataBase.loadUserDate(new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {

                                                progressDialog.dismiss();
                                                Intent i = new Intent(LoginPage.this, MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }

                                            @Override
                                            public void onFailure() {
                                                progressDialog.dismiss();
                                                Toast.makeText(LoginPage.this, "error fetch data", Toast.LENGTH_SHORT).show();

                                            }
                                        });


                                    }

                                    @Override
                                    public void onFailure() {

                                        progressDialog.dismiss();
                                        Toast.makeText(LoginPage.this, "user already exist", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            // not a new user
                            else {
                                DataBase.loadUserDate(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        DataBase.loadTestData(new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                progressDialog.dismiss();
                                                Intent i = new Intent(LoginPage.this, MainActivity.class);
                                                startActivity(i);
                                                finish();

                                            }

                                            @Override
                                            public void onFailure() {
                                                progressDialog.dismiss();
                                                Toast.makeText(LoginPage.this, "error fetching data", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure() {

                                        Toast.makeText(LoginPage.this, "error fetch data", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(LoginPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }


                });
    }


    // [END auth_with_google]


    protected void signInWithFirebase(String userEmail, String userPassword) {

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginPage.this, "Signed in successfully", Toast.LENGTH_SHORT).show();

                            DataBase.loadUserDate(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();
                                    Intent i = new Intent(LoginPage.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginPage.this, "error fetch data", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(LoginPage.this, "Error Signing in, please try again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(LoginPage.this, MainActivity.class);
            startActivity(i);

        }
    }
}