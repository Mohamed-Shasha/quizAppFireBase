package com.example.quizapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.quizapp.Adapter.RankAdapter;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

    private TextView totalUsers, imageTextInitial, imageTextName, score, rank;
    private RecyclerView usersView;
    private Dialog progressDialog;
    private TextView dialogText;
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

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("loading");
        progressDialog.show();

        totalUsers = view.findViewById(R.id.totalUsers);
        imageTextInitial = view.findViewById(R.id.statsInitialImageText);
        imageTextName = view.findViewById(R.id.statsImageText);
        score = view.findViewById(R.id.statsScoreImageText);
        rank = view.findViewById(R.id.statsRankImageText);
        usersView = view.findViewById(R.id.userView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        rankAdapter = new RankAdapter(DataBase.usersList);

        usersView.setAdapter(rankAdapter);
        progressDialog.show();
        DataBase.getTopUsers(new MyCompleteListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess() {
                rankAdapter.notifyDataSetChanged();
                if (DataBase.performance.getTotalScore() != 0) {
                    if (!DataBase.inTopList) {
                        myRankCal();
                    }
                    score.setText("Score" + DataBase.performance.getTotalScore());
                    rank.setText("Rank" + DataBase.performance.getRank());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {

                Toast.makeText(getContext(), "error fetch data", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
        totalUsers.setText("Total Users :" + DataBase.usersTotal);
        imageTextInitial.setText(DataBase.performance.getName().toUpperCase().substring(0, 1));


        return view;
    }

    private void myRankCal() {
        int lowestTopScore = DataBase.usersList.get(DataBase.usersList.size() - 1).getTotalScore();
//        check rank in remaining users / not in top 20
        int remaining = DataBase.usersTotal - 20;

        int mySlot = (DataBase.performance.getTotalScore() * remaining) / lowestTopScore;
        int rank;
        if (lowestTopScore != DataBase.performance.getTotalScore()) {
            rank = DataBase.usersTotal - mySlot;
        } else {
            rank = 21;
        }
        DataBase.performance.setRank(rank);


    }
}