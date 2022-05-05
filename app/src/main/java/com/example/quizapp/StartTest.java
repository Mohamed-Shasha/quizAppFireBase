package com.example.quizapp;

import static com.example.quizapp.DataBase.cat_index;
import static com.example.quizapp.DataBase.g_cat_List;
import static com.example.quizapp.DataBase.g_question_list;
import static com.example.quizapp.DataBase.loadQuestions;
import static com.example.quizapp.DataBase.selectedTestIndex;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.StringValue;

public class StartTest extends AppCompatActivity {

    private TextView st_cat_name, st_test_number, st_total_questions, st_best_score, st_time;
    private Button startTestButton;
    private ImageView backButton;

    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);


        st_cat_name = findViewById(R.id.st_category_name);
        st_test_number = findViewById(R.id.st_test_number);
        st_total_questions = findViewById(R.id.st_total_questions);
        st_best_score = findViewById(R.id.st_best_score);
        st_time = findViewById(R.id.st_time);
        startTestButton = findViewById(R.id.start_test);
        backButton = findViewById(R.id.st_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartTest.this, Questions.class);
                startActivity(i);
                finish();
            }
        });
        progressDialog = new Dialog(StartTest.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("Questions loading");
        progressDialog.show();


        loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                setDate();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(StartTest.this, "Error Fetching Data", Toast.LENGTH_LONG).show();

            }
        });



    }


// set the Test Data Page from Model
    private void setDate() {
        st_cat_name.setText(g_cat_List.get(cat_index).getCategoryName());
        st_test_number.setText("Test Number" + String.valueOf(selectedTestIndex+1));
        st_total_questions.setText(String.valueOf(g_question_list.size()));
        st_best_score.setText(String.valueOf(DataBase.g_test_List.get(selectedTestIndex).getTopScore()));
        st_time.setText(String.valueOf(DataBase.g_test_List.get(selectedTestIndex).getTime()));
    }
}