package com.example.quizapp.Model;

public class RankModel {
    private String name;
    private int totalScore;
    private int rank;

    public RankModel(int totalScore, int rank, String name) {
        this.totalScore = totalScore;
        this.rank = rank;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
