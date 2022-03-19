package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {

    private RecyclerView testView;
    private Toolbar toolbar;

    List<TestModel> testModelList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        int categoryIndex = getIntent().getIntExtra("CAT_INDEX", 0);
        getSupportActionBar().setTitle(CategoryFragment.categoryModelList.get(categoryIndex).getCategoryName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testView = findViewById(R.id.test_recycler_view);

        LinearLayoutManager layoutManager  = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);

        loadTest();

        TestAdapter adapter = new TestAdapter(testModelList);
        testView.setAdapter(adapter);


    }
//loadTest from Model
    private void loadTest() {
        testModelList = new ArrayList<>();
        testModelList.add(new TestModel("1",50,20));
        testModelList.add(new TestModel("2",40,20));
        testModelList.add(new TestModel("3",30,24));
        testModelList.add(new TestModel("4",80,22));
    }
// back button function
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Test.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}