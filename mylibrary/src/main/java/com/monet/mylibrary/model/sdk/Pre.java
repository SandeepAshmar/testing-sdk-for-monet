package com.monet.mylibrary.model.sdk;

import java.util.ArrayList;

public class Pre {
    private ArrayList<PreQuestions> questions;

    public ArrayList<PreQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<PreQuestions> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "ClassPojo [questions = " + questions + "]";
    }
}