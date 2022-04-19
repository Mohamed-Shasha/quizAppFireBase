package com.example.quizapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.quizapp.Adapter.RankAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

    private TextView totalUsers, imageTextInitial, imageTextName, score, rank;
    private RecyclerView usersView;

    private RankAdapter rankAdapter;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_stats, container, false);

//        Toolbar toolbar = findViewById(R.id.toolbar_test);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Stats");


        totalUsers = view.findViewById(R.id.totalUsers);
        imageTextInitial = view.findViewById(R.id.statsInitialImageText);
        imageTextName = view.findViewById(R.id.statsImageText);
        score = view.findViewById(R.id.statsScoreImageText);
        rank = view.findViewById(R.id.statsRankImageText);
        usersView = view.findViewById(R.id.userView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        rankAdapter  =new RankAdapter(DataBase.usersList);

        usersView.setAdapter(rankAdapter);

        DataBase.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                rankAdapter.notifyDataSetChanged();
                score.setText("Score" +DataBase.performance.getTotalScore());
                rank.setText("Score" +DataBase.performance.getRank());
            }

            @Override
            public void onFailure() {

            }
        });
        totalUsers.setText("Total Users :"+ DataBase.usersTotal);


        return view;
    }

    private void myRankCal(){

    }
}