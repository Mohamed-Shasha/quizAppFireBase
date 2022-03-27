package com.example.quizapp.Adapter;

public class RankModel {
    private int totalScore;
    private int rank;

    public RankModel(int totalScore, int rank) {
        this.totalScore = totalScore;
        this.rank = rank;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
