package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Questions extends AppCompatActivity {

    private RecyclerView questionView;
    private TextView question_ID, timer_question, question_cat_Name;
    private Button submit;
    private ImageButton previous_question, next_question, clear, flag;
    private ImageView question_List;
    private QuestionAdapter questionAdapter;
    private int questionID_Number;

    private DrawerLayout drawer;

    private GridView questionsGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_drawer);

//        initialize variables
        questionView = findViewById(R.id.question_view);
        question_cat_Name = findViewById(R.id.questionActivity_category);
        question_ID = findViewById(R.id.textView_question_no);
        question_List = findViewById(R.id.imageView_questionList);
        timer_question = findViewById(R.id.textView_question_timer);
        submit = findViewById(R.id.question_button_submit);
        previous_question = findViewById(R.id.imageButton_previous);
        next_question = findViewById(R.id.question_imageButton_next);
        flag = findViewById(R.id.question_imageButton_flag);
        clear = findViewById(R.id.question_imageButton_clear);
        drawer = findViewById(R.id.drawer_question);
        questionID_Number = 0;

        question_ID.setText("1/" + String.valueOf(DataBase.g_question_list.size()));
        question_cat_Name.setText(DataBase.g_cat_List.get(DataBase.cat_index).getCategoryName());

         questionAdapter = new QuestionAdapter(DataBase.g_question_list);
        questionView.setAdapter(questionAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionView.setLayoutManager(layoutManager);

        setSnapHelper();

        setClickListener();

        setTimer();
    }


    private void setSnapHelper() {


        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionView);

        questionView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                find snap and store it in View
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
//                get position of question view
                questionID_Number = recyclerView.getLayoutManager().getPosition(view);
                question_ID.setText(String.valueOf(questionID_Number + 1) + "/" + DataBase.g_question_list.size());

                if(DataBase.g_question_list.get(questionID_Number).getStatus()==DataBase.NOT_VISITED){
                    DataBase.g_question_list.get(questionID_Number).setStatus(DataBase.UNANSWERED);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }


    private void setClickListener() {

        previous_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                make sure that we are not on first question
                if (questionID_Number > 0) {

                    questionView.smoothScrollToPosition(questionID_Number - 1);
                }

            }
        });

        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                make sure that we are not on last question
                if (questionID_Number < DataBase.g_question_list.size() - 1) {

                    questionView.smoothScrollToPosition(questionID_Number + 1);
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase.g_question_list.get(questionID_Number).setSelectedAnswer(-1);
                questionAdapter.notifyDataSetChanged();
            }
        });

        question_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!drawer.isDrawerOpen(GravityCompat.END)){
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
    }


    private void setTimer() {

        long testTime = DataBase.g_test_List.get(DataBase.selectedTestIndex).getTime() * 60 * 1000;
        CountDownTimer countDownTimer = new CountDownTimer(testTime, 1000) {
            @Override
            public void onTick(long remainingTime) {

                @SuppressLint("DefaultLocale") String time = String.format("%02d: %02d minutes",
//                        minutes
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
//                        seconds
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );
                timer_question.setText(time);
            }

            @Override
            public void onFinish() {


            }
        };

        countDownTimer.start();
    }



}