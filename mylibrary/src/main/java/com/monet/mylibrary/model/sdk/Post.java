package com.monet.mylibrary.model.sdk;

import java.util.ArrayList;

public class Post {
    private ArrayList<PostQuestions> questions;

    public ArrayList<PostQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<PostQuestions> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "ClassPojo [questions = " + questions + "]";
    }
}
