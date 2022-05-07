package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.quizapp.Adapter.AnswersAdapter;
import com.google.firestore.v1.TargetOrBuilder;

import java.util.concurrent.TimeUnit;

public class Score extends AppCompatActivity {

    private TextView totalQuestions, score, timeTaken, correct, wrong, unattempted;
    private Button viewAnswers, reattempt;
    private long timeTakenCal;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Dialog progressDialog;
    private TextView dialogText;
    private int grade;
    private long TimeTakenCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        toolbar = findViewById(R.id.toolbar_score);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        initialize data
        progressDialog = new Dialog(Score.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("Loading..");

        progressDialog.show();
        score = findViewById(R.id.score);
        timeTaken = findViewById(R.id.time_taken);
        totalQuestions = findViewById(R.id.total_question_score);
        correct = findViewById(R.id.correct_answers);
        wrong = findViewById(R.id.wrong_answers);
        unattempted = findViewById(R.id.unattempted);
        viewAnswers = findViewById(R.id.checkAnswers);
        reattempt = findViewById(R.id.reattempt);


        loadScoreData();
        setBookmark();
        saveResult();
        reattempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reAttemptFunction();
            }
        });

        viewAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Score.this, Answers.class);
                startActivity(i);

            }
        });

    }

    private void setBookmark() {

        for (int i = 0; i < DataBase.g_question_list.size(); i++) {
            if (DataBase.g_question_list.get(i).isBookmarked()) {

//                this is bookmarked question with ID is not the bookmark list fetched shall to be added
                if (!DataBase.g_bookmarkIdList.contains(DataBase.g_question_list.get(i).getQuestionID())) {
                    DataBase.g_bookmarkIdList.add(DataBase.g_question_list.get(i).getQuestionID());
                    DataBase.profile.setBookmarkCount(DataBase.g_bookmarkIdList.size());
                }

            } else {

//                this is question with ID is not the list fetched shall to be removed if found in bookmark list
                if(DataBase.g_bookmarkIdList.contains(DataBase.g_question_list.get(i).getQuestionID())){
                    DataBase.g_bookmarkIdList.remove(DataBase.g_question_list.get(i).getQuestionID());
                    DataBase.profile.setBookmarkCount(DataBase.g_bookmarkIdList.size());
                }
            }
        }
    }



    private void reAttemptFunction() {
//        reset answers and their statuses
        for (int i = 0; i < DataBase.g_question_list.size(); i++) {
            DataBase.g_question_list.get(i).setSelectedAnswer(-1);
            DataBase.g_question_list.get(i).setStatus(DataBase.NOT_VISITED);

        }
//        take back to starting test page
        Intent i = new Intent(Score.this, StartTest.class);
        startActivity(i);
        finish();
    }

    private void loadScoreData() {
        int correctQuestion = 0;
        int wrongQuestion = 0;
        int unattemptedQuestion = 0;
//        loop through all question retrieved
        for (int i = 0; i < DataBase.g_question_list.size(); i++) {
//            depending on status we calculate number of correct and wrong and unattempted
            if (DataBase.g_question_list.get(i).getSelectedAnswer() == -1) {
                unattemptedQuestion++;
            } else {
                if (DataBase.g_question_list.get(i).getSelectedAnswer() == DataBase.g_question_list.get(i).getAnswer()) {
                    correctQuestion++;
                } else {
                    wrongQuestion++;
                }
            }
        }
//         set results data fields
        correct.setText(String.valueOf(correctQuestion));
        wrong.setText(String.valueOf(wrongQuestion));
        unattempted.setText(String.valueOf(unattemptedQuestion));
        totalQuestions.setText(String.valueOf(DataBase.g_question_list.size()));
//        calculate grade divide correct on number of question
        grade = correctQuestion * 100 / DataBase.g_question_list.size();
        score.setText(String.valueOf(grade));
        timeTakenCal = getIntent().getLongExtra("Time_Taken", 0);

//        calculate time taken
        String time = String.format("%02d: %02d ",
//                        get minutes
                TimeUnit.MILLISECONDS.toMinutes(timeTakenCal),
//                       get seconds
                TimeUnit.MILLISECONDS.toSeconds(timeTakenCal) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTakenCal)));

        timeTaken.setText(time);
    }

//    pass grade to database sendResult method
    private void saveResult() {
        DataBase.sendResult(grade, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Log.d("score", "" + grade);

            }

            @Override
            public void onFailure() {

                Toast.makeText(Score.this, "Error sending score to DB", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });

    }

//    send back to previous item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Score.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}