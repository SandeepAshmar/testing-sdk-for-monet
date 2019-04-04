package com.monet.mylibrary.model.sdk;

import java.util.ArrayList;

public class PostQuestions {
    private String question;

    private String qs_id;

    private ArrayList<Options> options;

    private ArrayList<Values> values;

    private String questionType;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQs_id() {
        return qs_id;
    }

    public void setQs_id(String qs_id) {
        this.qs_id = qs_id;
    }

    public ArrayList<Values> getValues() {
        return values;
    }

    public void setValues(ArrayList<Values> values) {
        this.values = values;
    }

    public ArrayList<Options> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Options> options) {
        this.options = options;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    @Override
    public String toString() {
        return "ClassPojo [question = " + question + ", qs_id = " + qs_id + ", options = " + options + ", questionType = " + questionType + "]";
    }
}