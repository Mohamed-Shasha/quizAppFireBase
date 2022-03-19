package com.example.quizapp;

public class CategoryModel {

    private String documentID ;
    private String categoryName;
    private int numOfTests;

    public CategoryModel(String documentID, String categoryName, int numOfTests) {
        this.documentID = documentID;
        this.categoryName = categoryName;
        this.numOfTests = numOfTests;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getNumOfTests() {
        return numOfTests;
    }

    public void setNumOfTests(int numOfTests) {
        this.numOfTests = numOfTests;
    }
}
