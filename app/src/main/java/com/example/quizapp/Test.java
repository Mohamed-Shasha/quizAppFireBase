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

import com.example.quizapp.Adapter.TestAdapter;

public class Test extends AppCompatActivity {

    private RecyclerView testView;
    private Toolbar toolbar;
    private TestAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar_test);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle(DataBase.g_cat_List.get(DataBase.cat_index).getCategoryName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
// initialize fields
        testView = findViewById(R.id.test_recycler_view);

        progressDialog = new Dialog(Test.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("Loading..");

        progressDialog.show();

//       get and  set the layout manager of the layout from the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);

//       load test data from fire_store database from class database
        DataBase.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {
//                after test data loaded load score
                DataBase.loadScore(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        adapter = new TestAdapter(DataBase.g_test_List);
                        testView.setAdapter(adapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
//error message on score
                        Toast.makeText(Test.this, "error load score data", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }

            @Override
            public void onFailure() {
// error message on loading test data
                Toast.makeText(Test.this, "error fetch data", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    // let user go back to the item came from
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Test.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


}