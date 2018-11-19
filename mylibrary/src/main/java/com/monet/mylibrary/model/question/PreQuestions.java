package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class PreQuestions {

    private ArrayList<String> question = new ArrayList<>();

    public ArrayList<String> getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question.add(question);
    }
}
