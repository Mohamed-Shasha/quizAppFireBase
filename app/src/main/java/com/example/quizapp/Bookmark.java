package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Adapter.AnswersAdapter;
import com.example.quizapp.Adapter.BookmarksAdapter;
import com.example.quizapp.Adapter.TestAdapter;


    public class Bookmark extends AppCompatActivity {
        private RecyclerView questionView;
        private Toolbar toolbar;
        private Dialog progressDialog;
        private TextView dialogText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bookmark);
            questionView = findViewById(R.id.bookmark_recycler_view);
            toolbar = findViewById(R.id.bookmark_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

            getSupportActionBar().setTitle("Bookmarks");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            questionView.setLayoutManager(layoutManager);

            progressDialog = new Dialog(Bookmark.this);
            progressDialog.setContentView(R.layout.dialog_layout);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogText = progressDialog.findViewById(R.id.textViewDialog);
            dialogText.setText("loading");
            progressDialog.show();

            DataBase.loadBookmarkedQues(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    BookmarksAdapter adapter = new BookmarksAdapter(DataBase.g_question_bookmarked);
                    questionView.setAdapter(adapter);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(Bookmark.this, "Error Fetching Data", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });



        }


        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                com.example.quizapp.Bookmark.this.finish();
            }
            return super.onOptionsItemSelected(item);
        }

    }