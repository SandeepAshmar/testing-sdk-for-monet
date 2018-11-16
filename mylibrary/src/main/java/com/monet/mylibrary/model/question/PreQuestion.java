package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class PreQuestion {
    ArrayList<String> question;

    public ArrayList<String> getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question.add(question);
    }
}
