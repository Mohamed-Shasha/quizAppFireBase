package com.example.quizapp;

import static com.example.quizapp.DataBase.ANSWERED;
import static com.example.quizapp.DataBase.NOT_VISITED;
import static com.example.quizapp.DataBase.REVIEW;
import static com.example.quizapp.DataBase.UNANSWERED;
import static com.example.quizapp.DataBase.g_question_list;
import static com.example.quizapp.DataBase.g_test_List;
import static com.example.quizapp.DataBase.selectedTestIndex;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quizapp.Adapter.QuestionAdapter;
import com.example.quizapp.Adapter.QuestionGridViewAdapter;

import java.util.concurrent.TimeUnit;

public class Questions extends AppCompatActivity {

    private RecyclerView questionView;
    private TextView question_ID, timer_question, question_cat_Name;
    private Button submit;
    private ImageView previous_question, next_question, flag_image, bookmarkImage;

    private LinearLayout question_List, clear, flag, bookmark;
    private QuestionAdapter questionAdapter;
    private int questionID_Number;
    long testTime;


    private DrawerLayout drawer;

    private GridView questionsGridView;
    private QuestionGridViewAdapter gridViewAdapter;
    private CountDownTimer countDownTimer;
    public long timeTaken;

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
        submit = findViewById(R.id.button_submit);
        previous_question = findViewById(R.id.imageButton_previous);
        next_question = findViewById(R.id.imageButton_next);
        flag = findViewById(R.id.question_imageButton_flag);
        clear = findViewById(R.id.question_imageButton_clear);
        drawer = findViewById(R.id.drawer_question);
        questionsGridView = findViewById(R.id.question_gridView);
        bookmark = findViewById(R.id.view_bookmark);
        bookmarkImage = findViewById(R.id.imageView_bookmark);
        flag_image = findViewById(R.id.mark);


        g_question_list.get(0).setStatus(UNANSWERED);

        questionID_Number = 0;

        question_ID.setText("1/" + String.valueOf(g_question_list.size()));
        question_cat_Name.setText(DataBase.g_cat_List.get(DataBase.cat_index).getCategoryName());
        if (g_question_list.get(0).isBookmarked()) {

            bookmarkImage.setImageResource(R.drawable.addedbookmark);
        } else {
            bookmarkImage.setImageResource(R.drawable.ic_bookmark);
        }

        questionAdapter = new QuestionAdapter(g_question_list);
        questionView.setAdapter(questionAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionView.setLayoutManager(layoutManager);

        gridViewAdapter = new QuestionGridViewAdapter(this, g_question_list.size());
        questionsGridView.setAdapter(gridViewAdapter);
        setSnapHelper();
        setTimer();

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
                if (questionID_Number < g_question_list.size() - 1) {

                    questionView.smoothScrollToPosition(questionID_Number + 1);
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                g_question_list.get(questionID_Number).setSelectedAnswer(-1);
                g_question_list.get(questionID_Number).setStatus(DataBase.UNANSWERED);
                flag_image.setVisibility(View.GONE);
                questionAdapter.notifyDataSetChanged();
            }
        });

        question_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawer.isDrawerOpen(GravityCompat.END)) {
                    gridViewAdapter.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_image.getVisibility() != View.VISIBLE) {
                    flag_image.setVisibility(View.VISIBLE);
                    g_question_list.get(questionID_Number).setStatus(REVIEW);
                } else {
                    flag_image.setVisibility(View.GONE);
                    if (g_question_list.get(questionID_Number).getSelectedAnswer() != -1) {
                        g_question_list.get(questionID_Number).setStatus(ANSWERED);
                    } else {
                        g_question_list.get(questionID_Number).setStatus(UNANSWERED);
                    }

                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();


            }
        });
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToBookmark();
            }
        });


    }

    private void addToBookmark() {

//        already bookedmarked
        if (g_question_list.get(questionID_Number).isBookmarked()) {

            g_question_list.get(questionID_Number).setBookmarked(false);
        } else {
//            add to bookmark
            g_question_list.get(questionID_Number).setBookmarked(true);
            bookmarkImage.setImageResource(R.drawable.addedbookmark);

        }
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
//                get position of question from position of view
                questionID_Number = recyclerView.getLayoutManager().getPosition(view);
                question_ID.setText(String.valueOf(questionID_Number + 1) + "/" + g_question_list.size());

                if (g_question_list.get(questionID_Number).getStatus() == NOT_VISITED) {
                    g_question_list.get(questionID_Number).setStatus(UNANSWERED);
                }
                if (g_question_list.get(questionID_Number).getStatus() == REVIEW) {
                    flag_image.setVisibility(View.VISIBLE);
                } else {
                    flag_image.setVisibility(View.GONE);
                }


                if (g_question_list.get(questionID_Number).isBookmarked()) {

                    bookmarkImage.setImageResource(R.drawable.addedbookmark);
                } else {
                    bookmarkImage.setImageResource(R.drawable.ic_bookmark);
                }

            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
        View view = LayoutInflater.from(Questions.this)
                .inflate(R.layout.submit_test, findViewById(R.id.layout_dialog_container_submit));
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        view.findViewById(R.id.cancel_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countDownTimer.cancel();
                dialog.dismiss();

                Intent i = new Intent(Questions.this, Score.class);
                long totalTime = (long) g_test_List.get(selectedTestIndex).getTime() * 60 * 1000;
                i.putExtra("Time_Taken", totalTime - timeTaken);
                startActivity(i);
                Questions.this.finish();

            }
        });
        dialog.show();
    }


    public void sendToQuestion(int pos) {
        questionView.smoothScrollToPosition(pos);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    private void setTimer() {

        long testTime = (long) g_test_List.get(selectedTestIndex).getTime() * 60 * 1000;
        countDownTimer = new CountDownTimer(testTime, 1000) {
            @Override
            public void onTick(long remainingTime) {
                timeTaken = remainingTime;
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
                Intent i = new Intent(Questions.this, Score.class);
                startActivity(i);
                Questions.this.finish();

            }
        };

        countDownTimer.start();
    }


}