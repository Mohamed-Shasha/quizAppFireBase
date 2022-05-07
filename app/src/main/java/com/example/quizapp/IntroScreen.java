package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class IntroScreen extends AppCompatActivity {

    TextView title;
    ImageView image;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        image = findViewById(R.id.imageViewSplash);
        title = findViewById(R.id.textViewSplash);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        title.startAnimation(animation);
        mAuth = FirebaseAuth.getInstance();

        // Access a Cloud Firestore instance from your Activity
        DataBase.db = FirebaseFirestore.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                logged in
                if (mAuth.getCurrentUser() != null) {
//                    load data
                    DataBase.loadUserDate( new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Intent i = new Intent(IntroScreen.this, MainActivity.class);
                            startActivity(i);
                            IntroScreen.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(IntroScreen.this,"Error fetching data",Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
//                    not logged in take to log in page
                    Intent i = new Intent(IntroScreen.this, LoginPage.class);
                    startActivity(i);
                    IntroScreen.this.finish();
                }

            }
        }, 3000);
    }
}